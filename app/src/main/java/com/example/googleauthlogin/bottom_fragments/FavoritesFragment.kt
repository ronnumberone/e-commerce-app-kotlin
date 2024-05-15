package com.example.googleauthlogin.bottom_fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googleauthlogin.ProductDetailActivity
import com.example.googleauthlogin.adapter.CartItemAdapter
import com.example.googleauthlogin.adapter.ProductAdapter
import com.example.googleauthlogin.database.UserHelper
import com.example.googleauthlogin.databinding.FragmentFavoritesBinding


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var userHelper: UserHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view: View = binding.root

        userHelper = UserHelper()

        binding.rvFavorite.layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        binding.rvFavorite.setHasFixedSize(true)

        userHelper.getFavorite { list ->
            val adapt = ProductAdapter(list)
            adapt.setOnItemClickListener(object : ProductAdapter.onItemClickListener {
                override fun onItemClick(
                    productId: String,
                    productName: String,
                    productCost: Double,
                    productDescription: String,
                    category: String,
                    productImg: String,
                    createAt: Long
                ) {
                    val intent = Intent(requireContext(), ProductDetailActivity::class.java)
                    intent.putExtra("productId", productId)
                    intent.putExtra("productName", productName)
                    intent.putExtra("productCost", productCost)
                    intent.putExtra("productDescription", productDescription)
                    intent.putExtra("category", category)
                    intent.putExtra("productImg", productImg)
                    intent.putExtra("createAt", createAt)
                    startActivity(intent)
                }
            })
            binding.rvFavorite.adapter = adapt
        }

        return view
    }
}