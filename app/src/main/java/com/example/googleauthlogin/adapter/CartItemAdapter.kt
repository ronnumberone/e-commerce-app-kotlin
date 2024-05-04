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
import com.example.googleauthlogin.model.CartItem
import com.example.googleauthlogin.model.Product
import java.util.concurrent.Executors

class CartItemAdapter (var list:ArrayList<CartItem>) : RecyclerView.Adapter<CartItemAdapter.ProductViewHolder>(){
    private var clickListener: onClickListener? = null

    interface onClickListener {
        fun onDeleteClick(id: String)
    }

    fun setOnClickListener(listener: onClickListener) {
        clickListener = listener
    }

    fun calculateTotalPrice(): Double {
        var totalPrice: Double = 0.0
        for (item in list) {
            totalPrice += item.cartItemCost * item.quantity
        }
        return totalPrice
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
//            itemView.findViewById<ImageButton>(R.id.deleteBtn).setOnClickListener {
//                val id = list[adapterPosition].cartItemId
//                clickListener?.onDeleteClick(id)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.itemView.apply {
            val productName = findViewById<TextView>(R.id.productName)
            val productCost = findViewById<TextView>(R.id.productCost)
            val quantityTv = findViewById<TextView>(R.id.quantityTv)
            val productImg = findViewById<ImageView>(R.id.productImg)

            productName.text = list[position].cartItemName
            productCost.text = "$" + list[position].cartItemCost.toString()
            quantityTv.text = list[position].quantity.toString()

            var image: Bitmap? = null
            val imgUrl = list[position].cartItemImg
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