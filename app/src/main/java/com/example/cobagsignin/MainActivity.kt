package com.example.cobagsignin


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*


const val RC_SIGN_IN = 123
class MainActivity : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        sign_in_button.visibility = View.VISIBLE
        sign_in_button.setSize(SignInButton.SIZE_STANDARD)

        sign_in_button.setOnClickListener{
            signIn()
        }

        btn_sign_out.setOnClickListener{
            signOut()
        }
    }

    override fun onStart() {
        super.onStart()
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(acct)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Fail", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null)
        }
    }

    private fun signIn(){
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
    }

    private fun updateUI(@Nullable account: GoogleSignInAccount?) {
        if (account != null) {
//            val personName = acct.displayName
//            val personGivenName = acct.givenName
//            val personFamilyName = acct.familyName
//            val personEmail = acct.email
//            val personId = acct.id
//            val personPhoto: Uri? = acct.photoUrl
            sign_in_button.visibility = View.GONE
            tv_name.text = account.displayName
            tv_name.visibility = View.VISIBLE
            btn_sign_out.visibility = View.VISIBLE
        } else {
          sign_in_button.visibility = View.VISIBLE
            tv_name.text = ""
            tv_name.visibility = View.GONE
            btn_sign_out.visibility = View.GONE
        }
    }


}