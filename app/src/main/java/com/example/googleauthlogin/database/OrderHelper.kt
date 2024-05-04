package com.example.googleauthlogin.database

import android.util.Log
import com.example.googleauthlogin.model.CartItem
import com.example.googleauthlogin.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderHelper {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val currentUser: FirebaseUser? = auth.currentUser
    private val db: DatabaseReference = FirebaseDatabase.getInstance().getReference("Order")

    fun saveOrderToPending(receiverName: String, address: String, email: String, phoneNumber: String, totalPayment: Double, orderList: ArrayList<CartItem>) {
        val orderId = db.push().key
        val userId = currentUser?.uid
        val dbPendingRef = db.child("pending").child(orderId!!)
        val order = Order(orderId, userId, receiverName, address, email, phoneNumber, totalPayment, null)
        dbPendingRef.setValue(order)
            .addOnSuccessListener {
                for (item in orderList) {
                    val itemId = item.cartItemId
                    itemId.let {
                        val cartItemRef = dbPendingRef.child("orderItems").child(it)
                        cartItemRef.setValue(item)
                    }
                }
            }
    }

    fun getPendingOrder(callback: (ArrayList<Order>) -> Unit) {
        val list: ArrayList<Order> = ArrayList()

        val userId = currentUser?.uid
        val dbOrdersRef = db.child("pending")

        dbOrdersRef.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (orderSnapshot in snapshot.children) {
                        val data = orderSnapshot.getValue(Order::class.java)
                        data?.let { list.add(it) }
                    }
                    callback(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý lỗi nếu cần
                }
            })
    }

    fun getShippingOrder(callback: (ArrayList<Order>) -> Unit) {
        val list: ArrayList<Order> = ArrayList()

        val userId = currentUser?.uid
        val dbOrdersRef = db.child("shipping")

        dbOrdersRef.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (orderSnapshot in snapshot.children) {
                        val data = orderSnapshot.getValue(Order::class.java)
                        data?.let { list.add(it) }
                    }
                    callback(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý lỗi nếu cần
                }
            })
    }


    fun getAllPendingOrder(callback: (ArrayList<Order>) -> Unit) {
        val list: ArrayList<Order> = ArrayList()

        val dbPendingRef = db.child("pending")
        dbPendingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val data = snap.getValue(Order::class.java)
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

    fun getAllShippingOrder(callback: (ArrayList<Order>) -> Unit) {
        val list: ArrayList<Order> = ArrayList()

        val dbPendingRef = db.child("pending")
        dbPendingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val data = snap.getValue(Order::class.java)
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

    fun transferOrderFromPendingToShipping(orderId: String) {
        val dbPendingRef = db.child("pending")
        val dbShippingRef = db.child("shipping")

        dbPendingRef.child(orderId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.value

                // Ghi dữ liệu vào nút đích
                dbShippingRef.child(orderId).setValue(data)
                    .addOnSuccessListener {
                        // Xóa dữ liệu từ nút nguồn sau khi di chuyển thành công (nếu cần)
                        dbPendingRef.child(orderId).removeValue()
                            .addOnSuccessListener {
                                // Di chuyển thành công
                            }
                            .addOnFailureListener { e ->
                                // Xử lý khi xóa dữ liệu từ nút nguồn thất bại
                            }
                    }
                    .addOnFailureListener { e ->
                        // Xử lý khi ghi dữ liệu vào nút đích thất bại
                    }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ nút nguồn
            }
        })
    }


}