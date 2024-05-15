package com.example.googleauthlogin

import android.content.Intent
import android.health.connect.datatypes.units.Length
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googleauthlogin.adapter.OrderItemAdapter
import com.example.googleauthlogin.adapter.ProductAdapter
import com.example.googleauthlogin.adapter.ProductManagementAdapter
import com.example.googleauthlogin.database.OrderHelper
import com.example.googleauthlogin.database.UserHelper
import com.example.googleauthlogin.databinding.ActivityPaymentBinding
import com.example.googleauthlogin.databinding.ActivityPendingBinding

class PendingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPendingBinding
    private lateinit var orderHelper: OrderHelper
    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderHelper = OrderHelper()
        userHelper = UserHelper()

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("loadFragment", "profileFragment")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        userHelper.isAdmin { isAdmin ->
            if( isAdmin) {
                orderHelper.getAllPendingOrder { list ->
                    binding.rvOrder.layoutManager = GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false)
                    binding.rvOrder.setHasFixedSize(true)
                    val adapt = OrderItemAdapter(list)
                    adapt.setOnClickListener(object : OrderItemAdapter.onClickListener{
                        override fun onConfirmClick(orderId: String) {
                            orderHelper.transferOrderFromPendingToShipping(orderId)
                            startActivity(Intent(this@PendingActivity, ShippingActivity::class.java))
                            finish()
                        }
                    })
                    binding.rvOrder.adapter = adapt
                }
            } else {
                orderHelper.getPendingOrder { list ->
                    binding.rvOrder.layoutManager = GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false)
                    binding.rvOrder.setHasFixedSize(true)
                    val adapt = OrderItemAdapter(list)
                    binding.rvOrder.adapter = adapt
                }
            }
        }

//        orderHelper.getPendingOrder { list ->
//            binding.rvOrder.layoutManager = GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false)
//            binding.rvOrder.setHasFixedSize(true)
//            val adapt = OrderItemAdapter(list)
//            binding.rvOrder.adapter = adapt
//        }
    }
}