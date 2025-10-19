package com.example.adminpanel


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminpanel.Adapter.OrderDetailsAdapter
import com.example.adminpanel.Adapter.PendingOrderAdapter
import com.example.adminpanel.databinding.ActivityOrderDetailsBinding
import com.example.adminpanel.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailsBinding
    private var userName: String?= null
    private var address:String? = null
    private var phoneNumber:String?= null
    private var totalPrice:String? = null
    private var productNames: ArrayList<String> = arrayListOf()
    private var productImages: ArrayList<String> = arrayListOf()
    private var productQuantities: ArrayList<Int> = arrayListOf()
    private var productPrice: ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    binding.backButton.setOnClickListener {
        finish()
    }
        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        /*val orderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
*/
        val orderDetails = intent.getSerializableExtra("userOrderDetails") as? OrderDetails

        if (orderDetails == null) {
            Toast.makeText(this, "Error loading order details", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
           /* userName = orderDetails.userName
            productNames = orderDetails.productNames as ArrayList<String>
            productImages = orderDetails.productImages as ArrayList<String>
            productQuantities = orderDetails.productQuantity as ArrayList<Int>
            productPrice = orderDetails.productPrices as ArrayList<String>
            address = orderDetails.address
            phoneNumber = orderDetails.phoneNumber
            totalPrice = orderDetails.totalPrice*/


        // ✅ Safely extract data
        userName = orderDetails.userName
        productNames = (orderDetails.productNames ?: arrayListOf()) as ArrayList<String>
        productImages = (orderDetails.productImages ?: arrayListOf()) as ArrayList<String> // ✅ fixed
        productQuantities = (orderDetails.productQuantity ?: arrayListOf()) as ArrayList<Int>
        productPrice = (orderDetails.productPrices ?: arrayListOf()) as ArrayList<String>
        address = orderDetails.address
        phoneNumber = orderDetails.phoneNumber
        totalPrice = orderDetails.totalPrice
            // Set user info on screen
            setUserDetails()

            // Setup recycler view for product details
            setAdapter()
        }
    private fun setUserDetails() {
        binding.name.text = userName
        binding.address.text = address
        binding.phone.text = phoneNumber
        binding.totalAmount.text = totalPrice ?: "0"
    }
    private fun setAdapter() {
             binding.orderDetailsRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this,productNames,productImages,productQuantities,productPrice)
        binding.orderDetailsRecyclerView.adapter = adapter
    }
}