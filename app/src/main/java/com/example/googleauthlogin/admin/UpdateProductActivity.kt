package com.example.googleauthlogin.admin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import com.example.googleauthlogin.database.ProductHelper
import com.example.googleauthlogin.databinding.ActivityUpdateProductBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.concurrent.Executors

class UpdateProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProductBinding
    private lateinit var productHelper: ProductHelper
    private var imageUri: Uri? = null
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productHelper = ProductHelper()

        storageReference = FirebaseStorage.getInstance().getReference("Images")

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val productId = intent.getStringExtra("productId")
        var productName = intent.getStringExtra("productName")
        var productCost = intent.getDoubleExtra("productCost", 0.0)
        var productDescription = intent.getStringExtra("productDescription")
        var category = intent.getStringExtra("category")
        var productImg = intent.getStringExtra("productImg")
        val createAt = intent.getLongExtra("createAt", 0)

        binding.productName.setText(productName)
        binding.productCost.setText(productCost.toString())
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

        if (category == "Bag") {
            binding.radBag.isChecked = true
        } else if (category == "Dresses") {
            binding.radDresses.isChecked = true
        } else if (category == "Glasses") {
            binding.radGlasses.isChecked = true
        } else if (category == "Pant") {
            binding.radPant.isChecked = true
        } else if (category == "Jeans") {
            binding.radJeans.isChecked = true
        } else if (category == "Shirt") {
            binding.radShirt.isChecked = true
        } else if (category == "Shoes") {
            binding.radShoes.isChecked = true
        } else if (category == "Short") {
            binding.radShort.isChecked = true
        } else if (category == "Sport") {
            binding.radSport.isChecked = true
        } else if (category == "Sweater") {
            binding.radSweater.isChecked = true
        }

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.productImg.setImageURI(it)
            if (it != null) {
                imageUri = it
            }
        }

        binding.uploadImgBtn.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.updateProductBtn.setOnClickListener {
            imageUri?.let {
                if (productId != null) {
                    storageReference.child(productId).putFile(it)
                        .addOnSuccessListener { task->
                            task.metadata!!.reference!!.downloadUrl
                                .addOnSuccessListener { url->
                                    val imgUrl = url.toString()
                                    productName = binding.productName.text.toString()
                                    productCost = binding.productCost.text.toString().toDouble()
                                    productDescription = binding.productDescription.text.toString()
                                    productHelper.saveProductToDatabase(productId, productName!!, productCost, productDescription!!, getCategory(), imgUrl, createAt)
                                    startActivity(Intent(this, ProductManagementActivity::class.java))
                                    finish()
                                }
                        }
                }
            }
        }
    }

    private fun getCategory(): String {
        if (binding.radBag.isChecked) {
            return "Bag"
        } else if (binding.radDresses.isChecked) {
            return "Dresses"
        } else if (binding.radGlasses.isChecked) {
            return "Glasses"
        } else if (binding.radPant.isChecked) {
            return "Pant"
        } else if (binding.radJeans.isChecked) {
            return "Jeans"
        } else if (binding.radShirt.isChecked) {
            return "Shirt"
        } else if (binding.radShoes.isChecked) {
            return "Shoes"
        } else if (binding.radShort.isChecked) {
            return "Short"
        } else if (binding.radSport.isChecked) {
            return "Sport"
        } else if (binding.radSweater.isChecked) {
            return "Sweater"
        }
        return ""
    }
}
