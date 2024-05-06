package com.example.googleauthlogin.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googleauthlogin.R
import com.example.googleauthlogin.adapter.NotificationAdapter
import com.example.googleauthlogin.adapter.ProductAdapter
import com.example.googleauthlogin.database.NotificationHelper
import com.example.googleauthlogin.databinding.ActivityAddProductBinding
import com.example.googleauthlogin.databinding.ActivityNotificationManagementBinding

class NotificationManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationManagementBinding
    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationHelper = NotificationHelper()

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.addNotificationBtn.setOnClickListener {
            openNotificationDialog()
        }

        notificationHelper.getAllNotifications { list ->
            binding.rvNotification.layoutManager = GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, true)
            binding.rvNotification.setHasFixedSize(true)
            val adapt = NotificationAdapter(list)
            binding.rvNotification.adapter = adapt
        }
    }

    private fun openNotificationDialog() {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.add_notification_dialog, null)
        dialog.setView(dialogView)
        dialog.setTitle("Add Notification")
        val alertDialog = dialog.create()
        alertDialog.show()

        val addNotificationBtn = dialogView.findViewById<AppCompatButton>(R.id.addBtn)
        val title = dialogView.findViewById<EditText>(R.id.title)
        val content = dialogView.findViewById<EditText>(R.id.content)
        addNotificationBtn.setOnClickListener {
            notificationHelper.addNotification(title.text.toString(), content.text.toString())
            alertDialog.dismiss()
            recreate()
        }
    }
}