package com.example.googleauthlogin.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.googleauthlogin.R
import com.example.googleauthlogin.model.Product
import java.util.concurrent.Executors

class ProductAdapter(var list:ArrayList<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(
            productId: String,
            productName: String,
            productCost: Long,
            productDescription: String,
            category: String,
            productImg: String,
            createAt: Long
        )
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener {
                val productName = list[adapterPosition].productName
                val productId = list[adapterPosition].productId
                val productCost = list[adapterPosition].productCost
                val productDescription = list[adapterPosition].productDescription
                val category = list[adapterPosition].category
                val productImg = list[adapterPosition].productImg
                val createAt = list[adapterPosition].createAt
                mListener.onItemClick(productId, productName, productCost, productDescription, category, productImg, createAt)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.itemView.apply {
            val productName = findViewById<TextView>(R.id.productName)
            val productCost = findViewById<TextView>(R.id.productCost)
            val productImg = findViewById<ImageView>(R.id.productImg)

            productName.text = list[position].productName
            productCost.text = "$" + list[position].productCost.toString()

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