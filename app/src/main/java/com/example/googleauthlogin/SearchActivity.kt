package com.example.googleauthlogin

import android.content.Intent
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
        val keywordSearch = intent?.getStringExtra("keywordSearch")

        binding.keywordSearch.text = keywordSearch
        if (productList != null) {
            val totalItem = productList.size
            binding.totalItem.text = totalItem.toString()

            // Danh sách sản phẩm không null, tiếp tục xử lý
            binding.rvProduct.layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
            binding.rvProduct.setHasFixedSize(true)
            val adapt = ProductAdapter(productList)
            adapt.setOnItemClickListener(object : ProductAdapter.onItemClickListener {
                override fun onItemClick(
                    productId: String,
                    productName: String,
                    productCost: Double,
                    productDescription: String,
                    category: String,
                    productImg: String,
                    createAt: Long
                ) {
                    val intent = Intent(this@SearchActivity, ProductDetailActivity::class.java)
                    intent.putExtra("productId", productId)
                    intent.putExtra("productName", productName)
                    intent.putExtra("productCost", productCost)
                    intent.putExtra("productDescription", productDescription)
                    intent.putExtra("category", category)
                    intent.putExtra("productImg", productImg)
                    intent.putExtra("createAt", createAt)
                    startActivity(intent)
                }
            })
            binding.rvProduct.adapter = adapt
        }
    }
}