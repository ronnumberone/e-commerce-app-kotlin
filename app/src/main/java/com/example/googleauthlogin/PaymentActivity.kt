package com.example.googleauthlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.googleauthlogin.database.OrderHelper
import com.example.googleauthlogin.database.UserHelper
import com.example.googleauthlogin.databinding.ActivityMainBinding
import com.example.googleauthlogin.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var userHelper: UserHelper
    private lateinit var orderHelper: OrderHelper

    var totalCost: Double = 0.0
    var shippingFee: Double = 0.0
    var discountFee: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userHelper = UserHelper()
        orderHelper = OrderHelper()

        totalCost = intent.getDoubleExtra("totalCost", 0.0)
        shippingFee = binding.shippingFee.text.split("$")[1].toDouble()

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.voucherBtn.setOnClickListener {
            selectVoucher()
        }

        binding.methodBtn.setOnClickListener {
            selectPaymentMethod()
        }

        binding.buyBtnA.setOnClickListener {
            userHelper.getCart { list ->
                if (list.size > 0) {
                    val receiverName = binding.nameEdt.text.toString()
                    val address = binding.addressEdt.text.toString()
                    val email = binding.emailEdt.text.toString()
                    val phoneNumber = binding.phoneNumberEdt.text.toString()
                    val totalPayment = binding.totalPayment1.text.split("$")[1].toDouble()
                    orderHelper.saveOrderToPending(
                        receiverName,
                        address,
                        email,
                        phoneNumber,
                        totalPayment,
                        list
                    )
                    userHelper.clearCart()
                    val intent = Intent(this, PendingActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        binding.totalCost.text = "$" + totalCost.toString()
        binding.totalPayment1.text = "$" + (totalCost + shippingFee - discountFee).toString()
        binding.totalPayment2.text = "$" + (totalCost + shippingFee - discountFee).toString()
    }

    private fun selectVoucher() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select one voucher")

        val dialogView = layoutInflater.inflate(R.layout.discount_dialog, null)
        builder.setView(dialogView)

        val radioFreeship50 = dialogView.findViewById<RadioButton>(R.id.freeship50)
        val radioFreeship20 = dialogView.findViewById<RadioButton>(R.id.freeship20)

        val dialog = builder.create()
        dialog.show()

        radioFreeship50.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.voucherBtn.text = "Freeship 50%"
                binding.voucherBtn.setTextColor(ContextCompat.getColor(this, R.color.orange))
                discountFee = shippingFee * 0.5
                binding.discountFee.text = "- $" + discountFee.toString()
                binding.totalPayment1.text =
                    "$" + (totalCost + shippingFee - discountFee).toString()
                binding.totalPayment2.text =
                    "$" + (totalCost + shippingFee - discountFee).toString()
                dialog.dismiss()
            }
        }

        radioFreeship20.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.voucherBtn.text = "Freeship 20%"
                binding.voucherBtn.setTextColor(ContextCompat.getColor(this, R.color.orange))
                discountFee = shippingFee * 0.2
                binding.discountFee.text = "- $" + discountFee.toString()
                binding.totalPayment1.text =
                    "$" + (totalCost + shippingFee - discountFee).toString()
                binding.totalPayment2.text =
                    "$" + (totalCost + shippingFee - discountFee).toString()
                dialog.dismiss()
            }
        }
    }

    private fun selectPaymentMethod() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select one method")

        val dialogView = layoutInflater.inflate(R.layout.payment_method_dialog, null)
        builder.setView(dialogView)

        val paymentOnDelivery = dialogView.findViewById<RadioButton>(R.id.paymentOnDelivery)

        val dialog = builder.create()
        dialog.show()

        paymentOnDelivery.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.methodBtn.text = "Payment on delivery"
                binding.methodBtn.setTextColor(ContextCompat.getColor(this, R.color.orange))
                dialog.dismiss()
            }
        }
    }
}