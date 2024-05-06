package com.example.googleauthlogin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.googleauthlogin.R
import com.example.googleauthlogin.model.Notification
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationAdapter (var list:ArrayList<Notification>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.NotificationViewHolder, position: Int) {
        holder.itemView.apply {
            val title = findViewById<TextView>(R.id.title)
            val content = findViewById<TextView>(R.id.content)
            val createdAt = findViewById<TextView>(R.id.createdAt)
            val createAtLong = list[position].createdAt

            val sdf = SimpleDateFormat("HH::mm MM-dd-yyyy", Locale.getDefault())
            val createdAtTime = sdf.format(Date(createAtLong!!))

            title.text = list[position].title
            content.text = list[position].content
            createdAt.text = createdAtTime
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}