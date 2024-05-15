package com.example.googleauthlogin.bottom_fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googleauthlogin.PaymentActivity
import com.example.googleauthlogin.adapter.CartItemAdapter
import com.example.googleauthlogin.database.UserHelper
import com.example.googleauthlogin.databinding.FragmentCartBinding


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var userHelper: UserHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        val view: View = binding.root

        if (!isAdded) {
            return view
        }

        userHelper = UserHelper()

        val context = requireContext()

        binding.rvCartItem.setHasFixedSize(true)
        userHelper.getCart { list ->
            val adapt = CartItemAdapter(list)
            binding.rvCartItem.layoutManager = GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, true)
            adapt.setOnClickListener(object : CartItemAdapter.onClickListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDeleteClick(cartItemId: String) {
                    userHelper.deleteCartById(cartItemId)
                    list.clear()
                }
            })
            binding.totalCost.setText("$" + adapt.calculateTotalPrice().toString())
            binding.rvCartItem.adapter = adapt
        }

        binding.buyBtnB.setOnClickListener {
            val intent = Intent(requireContext(), PaymentActivity::class.java)
            val totalCost = binding.totalCost.text.split("$")[1].toDouble()
            intent.putExtra("totalCost", totalCost)
            startActivity(intent)
        }

        return view
    }
}
