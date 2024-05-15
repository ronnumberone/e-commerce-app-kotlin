package com.example.googleauthlogin.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.googleauthlogin.MainActivity
import com.example.googleauthlogin.R
import com.example.googleauthlogin.database.UserHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Date

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        userHelper = UserHelper()

        val currentUser = auth.currentUser
        db = FirebaseDatabase.getInstance().getReference("User")

        if (currentUser != null) {
            val userId = currentUser.uid
            val userName = currentUser.displayName
            val userEmail = currentUser.email
            val userImgUrl = currentUser.photoUrl.toString()
            val createAt = Date().time

            userHelper.addUserToDatabase(userName!!, userImgUrl, userEmail!!, "customer", createAt)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("loadFragment", "profileFragment")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        val signInButton = findViewById<Button>(R.id.signInButton)
        signInButton.setOnClickListener {
            val intent = Intent(this, GoogleActivity::class.java)
            startActivity(intent)
        }

        val fbAuthButton = findViewById<View>(R.id.fbAuthButton) as Button
        fbAuthButton.setOnClickListener {
            val intent = Intent(this, FacebookActivity::class.java)
            startActivity(intent)
        }
    }
}

