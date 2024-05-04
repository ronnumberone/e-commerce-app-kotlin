package com.example.googleauthlogin

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googleauthlogin.adapter.ProductAdapter
import com.example.googleauthlogin.databinding.ActivityPaymentBinding
import com.example.googleauthlogin.databinding.ActivitySearchBinding
import com.example.googleauthlogin.model.Product

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val productList = intent?.getSerializableExtra("productList") as? ArrayList<Product>
        if (productList != null) {
            // Danh sách sản phẩm không null, tiếp tục xử lý
            binding.rvProduct.layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
            binding.rvProduct.setHasFixedSize(true)
            val adapt = ProductAdapter(productList)
            binding.rvProduct.adapter = adapt
        }
    }
}