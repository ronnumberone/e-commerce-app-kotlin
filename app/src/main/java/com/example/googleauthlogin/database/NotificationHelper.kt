package com.example.googleauthlogin.database

import android.util.Log
import com.example.googleauthlogin.model.Notification
import com.example.googleauthlogin.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationHelper {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val currentUser: FirebaseUser? = auth.currentUser
    private val db: DatabaseReference = FirebaseDatabase.getInstance().getReference("Notification")

    fun addNotification(title: String, content: String) {
        val currentTime = java.util.Date().time

        val sdf = SimpleDateFormat("HH:mm:ss MM-dd-yyyy", Locale.getDefault())
        val createAt = sdf.format(Date(currentTime))

        val notification = Notification(title, content, currentTime)

        db.child(createAt).setValue(notification)
            .addOnCompleteListener {
                Log.d("NotificationHelper", "Add completed")
            }
            .addOnFailureListener {
                Log.d("NotificationHelper", "add failed")
            }
    }

    fun getAllNotifications(callback: (ArrayList<Notification>) -> Unit) {
        val list: ArrayList<Notification> = ArrayList()

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val data = snap.getValue(Notification::class.java)
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