package com.example.googleauthlogin.model

import android.net.Uri
import java.util.Date
class User (
    var userId:String? = null,
    var userName:String? = null,
    var userImgUrl:String? = null,
    var userEmail:String? = null,
    var role:String? = null,
    var createAt:Long? = null,
    var cart: Map<String, CartItem>? = null
)