package com.example.googleauthlogin.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.googleauthlogin.MainActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

class FacebookActivity : AppCompatActivity() {

    private lateinit var callbackManager: CallbackManager
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Toast.makeText(
                        this@FacebookActivity,
                        "ccc",
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(
                        this@FacebookActivity,
                        "dd",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(
                        this,
                        "Authentication successfully.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
//                    startActivity(Intent(this, LoginActivity::class.java))
//                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        this,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}