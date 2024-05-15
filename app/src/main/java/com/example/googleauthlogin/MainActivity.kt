package com.example.googleauthlogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.googleauthlogin.bottom_fragments.CartFragment
import com.example.googleauthlogin.bottom_fragments.FavoritesFragment
import com.example.googleauthlogin.bottom_fragments.HomeFragment
import com.example.googleauthlogin.bottom_fragments.NotificationFragment
import com.example.googleauthlogin.bottom_fragments.ProfileFragment
import com.example.googleauthlogin.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private  lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val loadFragment = intent.getStringExtra("loadFragment")

        //Nav menu
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                }
                R.id.bottom_cart -> {
                    replaceFragment(CartFragment())
                }
                R.id.bottom_Notification -> {
                    replaceFragment(NotificationFragment())
                }
                R.id.bottom_favorites -> {
                    replaceFragment(FavoritesFragment())
                }
                R.id.bottom_profile -> {
                    replaceFragment(ProfileFragment())
                }
            }
            true
        }

        if (loadFragment == "profileFragment") {
            replaceFragment(ProfileFragment())
            bottomNavigationView.selectedItemId = R.id.bottom_profile
        } else if (loadFragment == "cartFragment") {
            replaceFragment(CartFragment())
            bottomNavigationView.selectedItemId = R.id.bottom_cart
        } else {
            replaceFragment(HomeFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()
    }
}

