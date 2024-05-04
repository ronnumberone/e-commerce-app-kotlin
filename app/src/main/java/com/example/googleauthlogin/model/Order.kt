package com.example.googleauthlogin.model

class Order(
    val orderId: String? = null,
    val userId: String? = null,
    val receiverName: String? = null,
    val address: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val totalPayment: Double? = null,
    var orderItems: Map<String, CartItem>? = null
)