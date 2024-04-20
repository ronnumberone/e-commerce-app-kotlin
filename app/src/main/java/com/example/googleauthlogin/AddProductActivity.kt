package com.example.googleauthlogin

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.googleauthlogin.database.ProductHelper
import com.example.googleauthlogin.databinding.ActivityAddProductBinding
import com.example.googleauthlogin.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID
import kotlin.math.log

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private var imageUri: Uri? = null
    private lateinit var storageReference: StorageReference
    private val db: DatabaseReference = FirebaseDatabase.getInstance().getReference("Product")
    private lateinit var productHelper: ProductHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)

        storageReference = FirebaseStorage.getInstance().getReference("Images")

        setContentView(binding.root)

        productHelper = ProductHelper()

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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

        binding.addProductBtn.setOnClickListener {
            val productId = db.push().key!!
            val productName = binding.productName.text.toString()
            val productDescription = binding.productDescription.text.toString()
            val productCost = binding.productCost.text.toString().toLong()
            val category = getCategory()

            imageUri?.let {
                storageReference.child(productId).putFile(it)
                    .addOnSuccessListener { task->
                        task.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener { url->
                                val imgUrl = url.toString()
                                val createAt = java.util.Date().time
                                productHelper.saveProductToDatabase(productId, productName, productCost, productDescription, category, imgUrl, createAt)
                                startActivity(Intent(this, ProductManagementActivity::class.java))
                                finish()
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
        }
        else if (binding.radGlasses.isChecked) {
            return "Glasses"
        }
        else if (binding.radPant.isChecked) {
            return "Pant"
        }
        else if (binding.radJeans.isChecked) {
            return "Jeans"
        }
        else if (binding.radShirt.isChecked) {
            return "Shirt"
        }
        else if (binding.radShoes.isChecked) {
            return "Shoes"
        }
        else if (binding.radShort.isChecked) {
            return "Short"
        }
        else if (binding.radSport.isChecked) {
            return "Sport"
        }
        else if (binding.radSweater.isChecked) {
            return "Sweater"
        }
        return ""
    }
}