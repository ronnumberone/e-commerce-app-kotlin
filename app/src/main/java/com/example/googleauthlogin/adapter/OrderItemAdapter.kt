package com.example.googleauthlogin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.googleauthlogin.R
import com.example.googleauthlogin.database.UserHelper
import com.example.googleauthlogin.model.Order

class OrderItemAdapter(var list:ArrayList<Order>) : RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>() {

    private var clickListener: onClickListener? = null
    var userHelper = UserHelper()

    interface onClickListener {
        fun onConfirmClick(orderId: String)
    }

    fun setOnClickListener(listener: onClickListener) {
        clickListener = listener
    }

    inner class OrderItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.findViewById<Button>(R.id.confirmBtn).setOnClickListener {
                val orderId = list[adapterPosition].orderId
                clickListener?.onConfirmClick(orderId!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemAdapter.OrderItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderItemAdapter.OrderItemViewHolder, position: Int) {
        holder.itemView.apply {
            val productList = findViewById<TextView>(R.id.productList)
            val receiverName = findViewById<TextView>(R.id.nameEdt)
            val email = findViewById<TextView>(R.id.emailEdt)
            val phoneNumber = findViewById<TextView>(R.id.phoneNumberEdt)
            val address = findViewById<TextView>(R.id.addressEdt)
            val totalPayment = findViewById<TextView>(R.id.totalPayment)
            val confirmBtn = findViewById<Button>(R.id.confirmBtn)

            receiverName.text = list[position].receiverName
            email.text = list[position].email
            phoneNumber.text = list[position].phoneNumber
            address.text = list[position].address
            totalPayment.text = list[position].totalPayment.toString()

            userHelper.isAdmin { isAdmin ->
                if( isAdmin) {
                    confirmBtn.visibility = View.VISIBLE
                }
            }

            val orderItems = list[position].orderItems

            if (orderItems != null) {
                val cartItemsStringBuilder = StringBuilder()
                for ((_, cartItem) in orderItems) {
                    cartItemsStringBuilder.append("Name of product: ${cartItem.cartItemName}, Quantity: ${cartItem.quantity}\n")
                }
                productList.text = cartItemsStringBuilder.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}