package com.example.googleauthlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googleauthlogin.adapter.OrderItemAdapter
import com.example.googleauthlogin.database.OrderHelper
import com.example.googleauthlogin.database.UserHelper
import com.example.googleauthlogin.databinding.ActivityProductDetailBinding
import com.example.googleauthlogin.databinding.ActivityShippingBinding

class ShippingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShippingBinding
    private lateinit var orderHelper: OrderHelper
    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShippingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderHelper = OrderHelper()
        userHelper = UserHelper()

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("loadFragment", "profileFragment")
            startActivity(intent)
        }

        userHelper.isAdmin { isAdmin ->
            if( isAdmin) {
                orderHelper.getAllShippingOrder { list ->
                    binding.rvOrder.layoutManager = GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false)
                    binding.rvOrder.setHasFixedSize(true)
                    val adapt = OrderItemAdapter(list)
                    adapt.setOnClickListener(object : OrderItemAdapter.onClickListener{
                        override fun onConfirmClick(orderId: String) {

                        }
                    })
                    binding.rvOrder.adapter = adapt
                }
            }else{
                orderHelper.getShippingOrder { list ->
                    binding.rvOrder.layoutManager = GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false)
                    binding.rvOrder.setHasFixedSize(true)
                    val adapt = OrderItemAdapter(list)
                    binding.rvOrder.adapter = adapt
                }
            }
        }
    }

}