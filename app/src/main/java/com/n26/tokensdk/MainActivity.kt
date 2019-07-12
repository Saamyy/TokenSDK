package com.n26.tokensdk

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.token.security.AKSCryptoEngineFactory
import io.token.security.UserAuthenticationStore
import io.token.browser.TokenBrowserFactory
import io.token.proto.common.alias.AliasProtos
import io.token.user.TokenClient
import android.widget.Toast
import android.content.Intent
import android.app.KeyguardManager
import android.content.Context


/*
* this class is just an example of how to create and get a member using token sdk */

class MainActivity : AppCompatActivity() {
    // this parameter is the only way to get the member data form token after it is created
//    private lateinit var memberID: String
    val CODE_AUTHENTICATION_VERIFICATION = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tokenClient = createTokenMember()
        Log.i("Tag", tokenClient.toString())

        val km = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (km.isKeyguardSecure) {

            val i = km.createConfirmDeviceCredentialIntent("Authentication required", "password")
            startActivityForResult(i, CODE_AUTHENTICATION_VERIFICATION)
        } else
            Toast.makeText(
                this,
                "No any security setup done by user(pattern or password or pin or fingerprint",
                Toast.LENGTH_SHORT
            ).show()




        /*
         * for the recover a user we need to ask about cryptoFactory object as it is protected now not public  */
//        val cryptoEngine = tokenClient.cryptoFactory.create(userMemberId)
//        val newKey = cryptoEngine.create(memberID).publicKeys
//        val authorization = tokenClient
//            .createRecoveryAuthorizationBlocking(userMemberId, newKey)

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CODE_AUTHENTICATION_VERIFICATION) {
            Toast.makeText(this, "Success: Verified user's identity", Toast.LENGTH_SHORT).show()
            val tokenClient = createTokenMember()
            Log.i("Tag", tokenClient.toString())


        } else {
            Toast.makeText(this, "Failure: Unable to verify user's identity", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createTokenMember(): TokenClient? {
        val userAuthStore = UserAuthenticationStore(120)

        val engine = AKSCryptoEngineFactory(
            applicationContext,
            userAuthStore
        )

        val browserFactory = TokenBrowserFactory(applicationContext)

        val tokenClient = TokenClient
            .builder()
            .devKey("4qY7lqQw8NOl9gng0ZHgT4xdiDqxqoGVutuZwrUYQsI")
            .withBrowserFactory(browserFactory)
            .connectTo(io.token.TokenClient.TokenCluster.SANDBOX)
            .build()


        val alias = AliasProtos.Alias.newBuilder()
            .setValue("mahmoud.samy@n26.con")
            .setType(AliasProtos.Alias.Type.EMAIL)
            // The Realm is required.
            .setRealm("token")
            .build()


//
//        tokenClient.createMember(alias).subscribe({
//            Log.d("token_creation", it.realmId())
////            memberID = it.memberId()
//        }, {
//            Log.e("token_creation", "error creatring member", it)
//        })




        return tokenClient
    }

}


