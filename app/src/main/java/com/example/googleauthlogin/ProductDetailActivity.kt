package com.example.googleauthlogin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.googleauthlogin.bottom_fragments.CartFragment
import com.example.googleauthlogin.database.UserHelper
import com.example.googleauthlogin.databinding.ActivityMainBinding
import com.example.googleauthlogin.databinding.ActivityProductDetailBinding
import java.util.concurrent.Executors

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userHelper = UserHelper()

        val productId = intent.getStringExtra("productId")
        val productName = intent.getStringExtra("productName")
        val productCost = intent.getDoubleExtra("productCost", 0.0)
        val productDescription = intent.getStringExtra("productDescription")
        val category = intent.getStringExtra("category")
        val productImg = intent.getStringExtra("productImg")
        val createAt = intent.getLongExtra("createAt", 0)
        var quantity = binding.quantityTv.text.toString().toInt()

        binding.plusBtn.setOnClickListener {
            if (binding.quantityTv.text.toString().toInt() < 99) {
                val currentQuantity = binding.quantityTv.text.toString().toInt() + 1
                binding.quantityTv.text = currentQuantity.toString()
                quantity = currentQuantity
            }
        }

        binding.minusBtn.setOnClickListener {
            if (binding.quantityTv.text.toString().toInt() > 1) {
                val currentQuantity = binding.quantityTv.text.toString().toInt() - 1
                binding.quantityTv.text = currentQuantity.toString()
                quantity = currentQuantity
            }
        }

        binding.addToCartBtn.setOnClickListener {
            userHelper.addToCart(productId!!, productName!!, productImg!!, productCost, quantity)
//            val intent = Intent(this, MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
            finish()
        }

        binding.favoriteBtn.setOnClickListener {
            userHelper.addToFavorite(productId!!, productName!!, productCost, productDescription!!, category!!, productImg!!, createAt)
            Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show()
        }

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.productName.setText(productName)
        binding.productCost.setText("$" + productCost.toString())
        binding.productDescription.setText(productDescription)
        binding.category.setText(category)

        var image: Bitmap? = null
        val executorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executorService.execute {
            try {
                val `in` = java.net.URL(productImg).openStream()
                image = BitmapFactory.decodeStream(`in`)
                handler.post {
                    binding.productImg.setImageBitmap(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}