package com.example.googleauthlogin.database

import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.googleauthlogin.model.Product
import com.example.googleauthlogin.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.Date
import java.util.Locale
import java.util.UUID

class ProductHelper {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val currentUser: FirebaseUser? = auth.currentUser
    private val db: DatabaseReference = FirebaseDatabase.getInstance().getReference("Product")

    fun saveProductToDatabase(productId: String,productName: String, productCost: Double, productDescription: String, category: String, productImg: String, createAt: Long) {
        val product = Product(productId, productName, productCost, productDescription, category, productImg, createAt)

         db.child(productId).setValue(product)
             .addOnCompleteListener {
                 Log.d("ProductHelper", "Save product completed")
             }
             .addOnFailureListener {
                 Log.d("ProductHelper", "Save product failed")
             }
    }

    fun getAllProducts(callback: (ArrayList<Product>) -> Unit) {
        val list: ArrayList<Product> = ArrayList()

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val data = snap.getValue(Product::class.java)
                        data?.let { list.add(it) }
                    }
                    callback(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần
            }
        })
    }

    fun deleteProduct(productId: String) {
           db.child(productId).removeValue()
               .addOnSuccessListener {
                   Log.d("ProductHelper", "Delete product completed")
               }
               .addOnFailureListener {
                   Log.d("ProductHelper", "Delete product failed")
               }
    }

    fun searchForProduct(productName: String, callback: (ArrayList<Product>) -> Unit) {
        val list: ArrayList<Product> = ArrayList()

        db.orderByChild("productName")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val data = snapshot.getValue(Product::class.java)
                        data?.let {
                            if (it.productName.lowercase().contains(productName.lowercase())) {
                                list.add(it)
                            }
                        }
                    }
                    callback(list)
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }

}