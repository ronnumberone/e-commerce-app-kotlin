package com.example.googleauthlogin.model

import java.io.Serializable

class Product (
    var productId: String = "",
    var productName: String = "",
    var productCost: Double = 0.0,
    var productDescription: String = "",
    var category: String = "",
    var productImg: String = "",
    var createAt: Long = 0
) : Serializable