package com.example.googleauthlogin.admin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googleauthlogin.MainActivity
import com.example.googleauthlogin.adapter.ProductManagementAdapter
import com.example.googleauthlogin.database.ProductHelper
import com.example.googleauthlogin.databinding.ActivityProductManagementBinding
import com.example.googleauthlogin.model.Product

class ProductManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductManagementBinding
    private lateinit var list: ArrayList<Product>
    private lateinit var productHelper: ProductHelper
    private lateinit var productList: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productHelper = ProductHelper()

        productHelper.getAllProducts { list ->
            binding.totalItem.text = list.size.toString()

            binding.rvProduct.layoutManager = LinearLayoutManager(this)
            binding.rvProduct.setHasFixedSize(true)
            val adapt = ProductManagementAdapter(list)
            adapt.setOnDeleteItemClickListener(object : ProductManagementAdapter.onDeleteClickListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onDeleteClick(id: String) {
                    val dialog = AlertDialog.Builder(this@ProductManagementActivity)
                        dialog.setTitle("Delete Product")
                        .setMessage("Are you sure want to delete this product?")
                        .setCancelable(false)
                        .setPositiveButton("Yes") { _,_ ->
                            productHelper.deleteProduct(id)
                            recreate()
                        }
                        .setNegativeButton("No"){ _,_ -> }
                        .setNeutralButton("Cancel"){_,_ ->}
                    dialog.create().show()
                }

                override fun onUpdateClick(
                    productId: String,
                    productName: String,
                    productCost: Double,
                    productDescription: String,
                    category: String,
                    productImg: String,
                    createAt: Long
                ) {
                    val intent = Intent(this@ProductManagementActivity, UpdateProductActivity::class.java)
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

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("loadFragment", "profileFragment")
            startActivity(intent)
        }
    }
}