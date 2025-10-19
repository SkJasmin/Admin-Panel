package com.example.adminpanel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminpanel.Adapter.PendingOrderAdapter
import com.example.adminpanel.databinding.ActivityPendingOrderBinding
import com.example.adminpanel.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.text.get

class PendingOrderActivity : AppCompatActivity(), PendingOrderAdapter.OnItemClicked {
    private  lateinit var binding: ActivityPendingOrderBinding
    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfTotalPrice: MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder: MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrderDetails> =arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Initalization of database
        database = FirebaseDatabase.getInstance()
        //Initalization of database Reference
        databaseOrderDetails = database.reference.child("OrderDetails")
        //get OrderDetails
        getOrderDetails()
        binding.backButton.setOnClickListener {
            finish()
        }
    }
    private fun getOrderDetails() {
        //Retrive order details from Firebase Database
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfOrderItem.clear()
                if (!snapshot.exists()) {
                    Toast.makeText(this@PendingOrderActivity, "No pending orders found", Toast.LENGTH_SHORT).show()
                    return
                }
                for(orderSnapshort in snapshot.children){
                    val orderDetails = orderSnapshort.getValue(OrderDetails::class.java)
                    orderDetails?.let{
                        listOfOrderItem.add(it)
                    }
                }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PendingOrderActivity, "Failed to load orders: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun addDataToListForRecyclerView() {
 for(orderItem in listOfOrderItem){
     //add data to respective list for populating the recyclerView
     orderItem.userName?.let { listOfName.add(it) }
     orderItem.totalPrice?.let { listOfTotalPrice.add(it) }
     /*orderItem.productImages?.filterNot { it.isEmpty() }?.forEach {
         listOfImageFirstFoodOrder.add(it)*/
     orderItem.productImages?.firstOrNull()?.let { listOfImageFirstFoodOrder.add(it)
 }
 }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = PendingOrderAdapter(this,listOfName,listOfTotalPrice,listOfImageFirstFoodOrder,this)
        binding.pendingRecyclerView.adapter = adapter
    }

    override fun onItemClickListener(position: Int) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("userOrderDetails", userOrderDetails)// keep consistent key name
        startActivity(intent)
    }

    override fun onItemAcceptClickListener(position: Int) {
        //handle item acceptence and update database
        val childItemPushKey = listOfOrderItem[position].itemPushKey
        val clickItemOrderReference = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderReference?.child("orderAccepted")?.setValue(true)
        updateOrderAccepetStatus(position)
    }

    override fun onItemDispatchClickListener(position: Int) {
        //handle item dispatch and update database
        val dispatchItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatchItemOrderReference = database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemFromOrderDetails(dispatchItemPushKey)
            }

    }
    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {
          val orderDetailsItemsReference = database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemsReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Order is Dispatched", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Order is not Dispatched", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateOrderAccepetStatus(position: Int) {
      //Update order acceptance in user's BuyHistory and OrderDetails
        val userIdOfClickedItem = listOfOrderItem[position].userUid
        val pushKeyOfClickedItem = listOfOrderItem[position].itemPushKey
        val buyHistoryReference = database.reference.child("user").child(userIdOfClickedItem!!).child("ByHistory").child(pushKeyOfClickedItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)

    }
}

