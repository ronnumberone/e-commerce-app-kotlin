package com.example.googleauthlogin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

        var productId = intent.getStringExtra("productId")
        var productName = intent.getStringExtra("productName")
        var productCost = intent.getLongExtra("productCost", 0)
        var productDescription = intent.getStringExtra("productDescription")
        var category = intent.getStringExtra("category")
        var productImg = intent.getStringExtra("productImg")
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
            val totalCost = productCost * quantity
            userHelper.addToCart(productId!!, productName!!, productImg!!, totalCost, quantity)
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.productName.setText(productName)
        binding.productCost.setText("$" + productCost.toString())
        binding.productDescription.setText(productDescription)

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