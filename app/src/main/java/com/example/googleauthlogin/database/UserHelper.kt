package com.example.googleauthlogin.database

import android.util.Log
import android.widget.Toast
import com.example.googleauthlogin.model.CartItem
import com.example.googleauthlogin.model.Product
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

    fun addUserToDatabase(
        userName: String?,
        userImgUrl: String,
        userEmail: String?,
        role: String,
        createAt: Long
    ) {
        if (currentUser != null) {
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

    fun addToCart(
        cartItemId: String,
        cartItemName: String,
        cartItemImg: String,
        cartItemCost: Double,
        quantity: Int
    ) {
        if (currentUser != null) {
            val userId = currentUser.uid
            val cartRef = db.child(userId).child("cart")
            cartRef.child(cartItemId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Sản phẩm tồn tại trong giỏ hàng, cộng mới số lượng
                        val cartItemMap = snapshot.value as? HashMap<String, Any>
                        val currentQuantity = cartItemMap?.get("quantity") as? Long ?: 0
                        val newQuantity = currentQuantity.toInt() + quantity
                        val cartItem = CartItem(
                            cartItemId,
                            cartItemName,
                            cartItemImg,
                            cartItemCost,
                            newQuantity
                        )
                        cartRef.child(cartItemId).setValue(cartItem)
                        Log.d("UserHelper", "Product exists, update quantity")
                    } else {
                        val cartItem =
                            CartItem(cartItemId, cartItemName, cartItemImg, cartItemCost, quantity)
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

    fun getCart(callback: (ArrayList<CartItem>) -> Unit) {
        val list: ArrayList<CartItem> = ArrayList()

        if (currentUser != null) {
            val userId = currentUser.uid
            val dbCartRef = db.child(userId).child("cart")
            dbCartRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {
                            val data = snap.getValue(CartItem::class.java)
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
    }

    fun clearCart() {
        val userId = currentUser?.uid
        val dbCartRef = db.child(userId!!).child("cart")

        dbCartRef.removeValue()
            .addOnSuccessListener {
                // Xóa thành công
                Log.d("UserHelper", "Deleted cart")
            }
            .addOnFailureListener { e ->
                // Xử lý khi xóa thất bại
                Log.e("UserHelper", "Delete cart failed: ${e.message}")
            }
    }

    fun isAdmin(callback: (Boolean) -> Unit) {
        if (currentUser != null) {
            db.child(currentUser.uid).get().addOnSuccessListener { dataSnapshot ->
                val user = dataSnapshot.getValue(User::class.java)
                val isAdmin = user?.role == "admin"
                callback(isAdmin)
            }.addOnFailureListener { e ->
                Log.e("firebase", "Error getting data", e)
                callback(false)
            }
        } else {
            callback(false)
        }
    }

}