package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.login

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        startFirebaseLoginIntent()
    }

    override fun onBackPressed() {
        //to nothing
    }

    private fun startFirebaseLoginIntent() {
        val providers = Arrays.asList(AuthUI.IdpConfig.EmailBuilder().build())
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser as FirebaseUser
                UserData.setFirebaseUser(user)
                setResult(RESULT_OK)
                finish()
            } else {
                //todo
                Toast.makeText(this, "Unable to log-in, try again.", Toast.LENGTH_LONG).show()
                startFirebaseLoginIntent()
            }
        }
    }
}
