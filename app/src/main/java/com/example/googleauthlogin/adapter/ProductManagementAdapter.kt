package com.example.googleauthlogin.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.googleauthlogin.R
import com.example.googleauthlogin.model.Product
import java.util.concurrent.Executors

class ProductManagementAdapter(var list:ArrayList<Product>) : RecyclerView.Adapter<ProductManagementAdapter.ProductViewHolder>(){

    private var deleteListener: onDeleteClickListener? = null

    interface onDeleteClickListener {
        fun onDeleteClick(id: String)
        fun onUpdateClick(productId: String, productName: String, productCost: Long, productDescription: String, category: String, productImg: String, createAt: Long)
    }

    fun setOnDeleteItemClickListener(listener: onDeleteClickListener) {
        deleteListener = listener
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.findViewById<ImageButton>(R.id.deleteBtn).setOnClickListener {
                val id = list[adapterPosition].productId
                deleteListener?.onDeleteClick(id)
            }

            itemView.findViewById<ImageButton>(R.id.updateBtn).setOnClickListener {
                val productId = list[adapterPosition].productId
                val productName = list[adapterPosition].productName
                val productCost = list[adapterPosition].productCost
                val productDescription = list[adapterPosition].productDescription
                val category = list[adapterPosition].category
                val productImg = list[adapterPosition].productImg
                val createAt = list[adapterPosition].createAt
                deleteListener?.onUpdateClick(productId, productName, productCost, productDescription, category, productImg, createAt)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_manage_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.itemView.apply {
            val productName = findViewById<TextView>(R.id.productName)
            val productId = findViewById<TextView>(R.id.productId)
            val productCost = findViewById<TextView>(R.id.productCost)
            val productImg = findViewById<ImageView>(R.id.productImg)
            val category = findViewById<TextView>(R.id.category)

            productName.text = list[position].productName
            productCost.text = "$" + list[position].productCost.toString()
            productId.text = list[position].productId
            category.text = list[position].category

            var image: Bitmap? = null
            val imgUrl = list[position].productImg
            val executorService = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            executorService.execute {
                try {
                    val `in` = java.net.URL(imgUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    handler.post {
                        productImg.setImageBitmap(image)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}