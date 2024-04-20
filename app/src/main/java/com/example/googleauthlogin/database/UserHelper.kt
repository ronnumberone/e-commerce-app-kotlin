package com.example.googleauthlogin.database

import android.util.Log
import android.widget.Toast
import com.example.googleauthlogin.model.CartItem
import com.example.googleauthlogin.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserHelper {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val currentUser: FirebaseUser? = auth.currentUser
    private val db: DatabaseReference = FirebaseDatabase.getInstance().getReference("User")

    fun addUserToDatabase(userName: String?, userImgUrl: String, userEmail: String?, role: String, createAt: Long) {
        if (currentUser != null ) {
            val userId = currentUser.uid
            val user = User(userId, userName, userImgUrl, userEmail, role, createAt, null)
            db.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        // Người dùng chưa tồn tại trong cơ sở dữ liệu, thêm người dùng mới vào
                        db.child(userId).setValue(user)
                            .addOnCompleteListener {
                                Log.d("UserHelper", "Add completed")
                            }
                            .addOnFailureListener {
                                Log.d("UserHelper", "add failed")
                            }
                    } else {
                        // Người dùng đã tồn tại trong cơ sở dữ liệu, không thêm mới
                        Log.d("UserHelper", "user exists")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("UserHelper", error.toString())
                }
            })
        }
    }

    fun addToCart(cartItemId: String, cartItemName: String, cartItemImg: String, totalCost: Long, quantity: Int) {
        if (currentUser != null ) {
            val userId = currentUser.uid
            val cartRef =  db.child(userId).child("cart")
            cartRef.child(cartItemId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        // Sản phẩm tồn tại trong giỏ hàng, cộng mới số lượng
                        val cartItemMap = snapshot.value as? HashMap<String, Any>
                        val currentQuantity = cartItemMap?.get("quantity") as? Long ?: 0
                        val currentTotalCost = cartItemMap?.get("totalCost") as? Long ?: 0
                        Log.d("UserHelper", currentTotalCost.toString())
                        val newQuantity = currentQuantity.toInt() + quantity
                        val newTotalCost = currentTotalCost + totalCost
                        val cartItem = CartItem(cartItemId, cartItemName, cartItemImg, newTotalCost, newQuantity)
                        cartRef.child(cartItemId).setValue(cartItem)
                        Log.d("UserHelper", "Product exists, update quantity")
                    } else {
                        val cartItem = CartItem(cartItemId, cartItemName, cartItemImg, totalCost, quantity)
                        cartRef.child(cartItemId).setValue(cartItem)
                        Log.d("UserHelper", "Product added")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("UserHelper", error.toString())
                }
            })
        }
    }

    suspend fun isAdmin(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (currentUser!= null) {
                    val dataSnapshot = db.child(currentUser.uid).get().await()
                    val user = dataSnapshot.getValue(User::class.java)
                    user?.role == "admin"
                }else {
                    false
                }
            } catch (e: Exception) {
                Log.e("firebase", "Error getting data", e)
                false
            }
        }
    }

}