package com.example.googleauthlogin.bottom_fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.googleauthlogin.AddProductActivity
import com.example.googleauthlogin.MainActivity
import com.example.googleauthlogin.ProductManagementActivity
import com.example.googleauthlogin.R
import com.example.googleauthlogin.database.UserHelper
import com.example.googleauthlogin.databinding.FragmentProfileBinding
import com.example.googleauthlogin.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var userHelper: UserHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view: View = binding.root

        userHelper = UserHelper()

        val loginBtn = view.findViewById<Button>(R.id.loginBtn)

        val logOutBtn = view.findViewById<Button>(R.id.logoutBtn)
        val profileImage = view.findViewById<CircleImageView>(R.id.profile_image)
        val userName = view.findViewById<TextView>(R.id.userName)
        val userEmail = view.findViewById<TextView>(R.id.userEmail)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        loginBtn.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        logOutBtn.setOnClickListener {
            signOut()
        }

        binding.productManagementBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ProductManagementActivity::class.java))
        }


        CoroutineScope(Dispatchers.Main).launch {
            if( userHelper.isAdmin()) {
                binding.productManagementBtn.visibility = View.VISIBLE
                binding.addProductBtn.visibility = View.VISIBLE
            }
        }

        binding.addProductBtn.setOnClickListener{
            startActivity(Intent(requireContext(), AddProductActivity::class.java))
        }

        if (user != null) {
            userName.visibility = View.VISIBLE
            userEmail.visibility = View.VISIBLE
            logOutBtn.visibility = View.VISIBLE
            loginBtn.visibility = View.GONE
            userName.text = user.displayName
            userEmail.text = user.email
            val userImg = user.photoUrl
            var image: Bitmap? = null
            val imgUrl = userImg.toString()
            val executorService = Executors.newSingleThreadExecutor()

            //set google image view
            executorService.execute {
                try {
                    val `in` = java.net.URL(imgUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            requireActivity().runOnUiThread {
                try {
                    Thread.sleep(1000)
                    profileImage.setImageBitmap(image)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

        } else {
            // Handle the case where the user is not signed in
        }

        return view
    }

    private fun signOut() {
        auth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // Đăng xuất thành công, chuyển sang màn hình đăng nhập
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.putExtra("loadFragment", "profileFragment")
                startActivity(intent)
            } else {
                // Xử lý khi có lỗi xảy ra trong quá trình đăng xuất
                Toast.makeText(requireContext(), "Đã xảy ra lỗi khi đăng xuất", Toast.LENGTH_SHORT).show()
            }
        }
    }
}