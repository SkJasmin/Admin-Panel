package com.example.adminpanel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminpanel.databinding.ActivityMainBinding
import com.example.adminpanel.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completedOrderReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.delivery.setOnClickListener{
            val intent = Intent(this,OutForDeliveryActivity::class.java)
            startActivity(intent)
        }
        binding.addProducts.setOnClickListener{
            val intent = Intent(this,AddProductActivity::class.java)
            startActivity(intent)
        }
        binding.allProducts.setOnClickListener{
            val intent = Intent(this,AllProductsActivity::class.java)
            startActivity(intent)
        }
        binding.profile.setOnClickListener{
            val intent = Intent(this,AdminProfileActivity::class.java)
            startActivity(intent)
        }
        binding.createNewUser.setOnClickListener{
            val intent = Intent(this,CreateUserActivity::class.java)
            startActivity(intent)
        }
        binding.pendingOrderTextView.setOnClickListener{
            val intent = Intent(this,PendingOrderActivity::class.java)
            startActivity(intent)
        }
        binding.logoutButton.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
        pendingOrders()

        completedOrders()

        wholeTimeErning()

    }

    private fun wholeTimeErning() {
        val listPfTotalPay = mutableListOf<Int>()
        completedOrderReference = FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(orderSnapshort in snapshot.children){
                    var completeOrder = orderSnapshort.getValue(OrderDetails::class.java)

                    completeOrder?.totalPrice?.replace("₹","")
                        ?.trim()
                        ?.toIntOrNull()
                        ?.let { listPfTotalPay.add(it) }
                    // ✅ Optional: debug log
                    android.util.Log.d("EARNINGS", "Order Total Price: ${completeOrder?.totalPrice}")

                }
                // ✅ Update TextView safely (make sure you're on UI thread)
                binding.wholeTimeEarning.text = listPfTotalPay.sum().toString() + "₹"


            }

            override fun onCancelled(error: DatabaseError) {
                android.util.Log.e("EARNINGS", "Error fetching earnings: ${error.message}")
            }
        })
    }

    private fun completedOrders() {
        database = FirebaseDatabase.getInstance()
        val completeOrderReference = database.reference.child("CompletedOrder")
        var completeOrderItemCount = 0
        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completeOrderItemCount = snapshot.childrenCount.toInt()
                binding.completeOrders.text = completeOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun pendingOrders() {
       database = FirebaseDatabase.getInstance()
        val pendingOrderReference = database.reference.child("OrderDetails")
        var pendingOrderItemCount = 0
        pendingOrderReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount = snapshot.childrenCount.toInt()
                binding.pendingOrders.text = pendingOrderItemCount.toString()
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}