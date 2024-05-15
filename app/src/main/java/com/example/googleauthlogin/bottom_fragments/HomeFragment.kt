package com.example.googleauthlogin.bottom_fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.googleauthlogin.ProductDetailActivity
import com.example.googleauthlogin.R
import com.example.googleauthlogin.SearchActivity
import com.example.googleauthlogin.adapter.ProductAdapter
import com.example.googleauthlogin.database.ProductHelper
import com.example.googleauthlogin.databinding.FragmentHomeBinding
import com.example.googleauthlogin.model.Product

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var productHelper: ProductHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view: View = binding.root

        productHelper = ProductHelper()

        val imageSlider = view.findViewById<ImageSlider>(R.id.imageSlider)
        val slideModels = ArrayList<SlideModel>()

        slideModels.add(SlideModel(R.drawable.slideshow1, ScaleTypes.FIT))
        slideModels.add(SlideModel(R.drawable.slideshow2, ScaleTypes.FIT))
        slideModels.add(SlideModel(R.drawable.slideshow3, ScaleTypes.FIT))
        slideModels.add(SlideModel(R.drawable.slideshow4, ScaleTypes.FIT))
        slideModels.add(SlideModel(R.drawable.slideshow5, ScaleTypes.FIT))
        slideModels.add(SlideModel(R.drawable.slideshow6, ScaleTypes.FIT))

        imageSlider.setImageList(slideModels, ScaleTypes.FIT)

        binding.rvProduct.layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        binding.rvProduct.setHasFixedSize(true)

        productHelper.getAllProducts { list ->
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
            binding.rvProduct.adapter = adapt
        }

        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.searchView.isIconified = true
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Xử lý tìm kiếm khi người dùng nhấn nút tìm kiếm trên bàn phím hoặc trong giao diện SearchView
                if (!query.isNullOrBlank()) {
                    productHelper.searchForProduct(query) { list ->
                        val intent = Intent(requireContext(), SearchActivity::class.java)
                        intent.putExtra("productList", list)
                        intent.putExtra("keywordSearch", query)
                        startActivity(intent)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        setCategoryOnClick()

        return view
    }

    private fun setCategoryOnClick() {
        binding.glassesBtn.setOnClickListener { onCategoryButtonClick(binding.glassesBtn.text.toString()) }
        binding.bagBtn.setOnClickListener { onCategoryButtonClick(binding.bagBtn.text.toString()) }
        binding.dressesBtn.setOnClickListener { onCategoryButtonClick(binding.dressesBtn.text.toString()) }
        binding.pantBtn.setOnClickListener { onCategoryButtonClick(binding.pantBtn.text.toString()) }
        binding.jeansBtn.setOnClickListener { onCategoryButtonClick(binding.jeansBtn.text.toString()) }
        binding.shirtBtn.setOnClickListener { onCategoryButtonClick(binding.shirtBtn.text.toString()) }
        binding.shoesBtn.setOnClickListener { onCategoryButtonClick(binding.shoesBtn.text.toString()) }
        binding.shortBtn.setOnClickListener { onCategoryButtonClick(binding.shortBtn.text.toString()) }
        binding.sportBtn.setOnClickListener { onCategoryButtonClick(binding.sportBtn.text.toString()) }
        binding.sweaterBtn.setOnClickListener { onCategoryButtonClick(binding.sweaterBtn.text.toString()) }
    }

    private fun onCategoryButtonClick(category: String) {
        productHelper.getProductsByCategory(category) { list ->
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.putExtra("productList", list)
            intent.putExtra("keywordSearch", category)
            startActivity(intent)
        }
    }


}