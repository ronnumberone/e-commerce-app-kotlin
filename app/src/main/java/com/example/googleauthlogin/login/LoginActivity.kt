package com.example.googleauthlogin.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.googleauthlogin.MainActivity
import com.example.googleauthlogin.R
import com.example.googleauthlogin.database.UserHelper
import com.example.googleauthlogin.model.User
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference
    val callbackManager = CallbackManager.Factory.create()
    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        userHelper = UserHelper()

        val currentUser = auth.currentUser
        db = FirebaseDatabase.getInstance().getReference("User")

        if (currentUser != null) {
            // The user is already signed in, navigate to MainActivity
            val userId = currentUser.uid
            val userName = currentUser.displayName
            val userEmail = currentUser.email
            val userImgUrl = currentUser.photoUrl.toString()
            val createAt = Date().time

            userHelper.addUserToDatabase(userName!!, userImgUrl, userEmail!!, "customer", createAt)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("loadFragment", "profileFragment")
            startActivity(intent)
            finish() // finish the current activity to prevent the user from coming back to the SignInActivity using the back button
        }

        val signInButton = findViewById<Button>(R.id.signInButton)
        signInButton.setOnClickListener {
            val intent = Intent(this, GoogleActivity::class.java)
            startActivity(intent)
        }

        val fbAuthButton = findViewById<View>(R.id.fbAuthButton) as Button
        fbAuthButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("user_location"))
        }

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {

                }

                override fun onError(exception: FacebookException) {

                }
            })

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        auth = Firebase.auth
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Facebook Authentication successfully.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        baseContext,
                        "Facebook Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

}

