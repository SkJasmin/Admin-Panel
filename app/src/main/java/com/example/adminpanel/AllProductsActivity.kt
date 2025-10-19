package com.example.adminpanel

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminpanel.Adapter.MenuItemAdapter
import com.example.adminpanel.databinding.ActivityAllProductsBinding
import com.example.adminpanel.model.AllProduct
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.log

class AllProductsActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private  var menuItems: ArrayList<AllProduct> = ArrayList()
    private val binding : ActivityAllProductsBinding by lazy{
        ActivityAllProductsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        databaseReference = FirebaseDatabase.getInstance().reference
        retrieveMenuItem()
        binding.backButton.setOnClickListener {
            finish()
        }

    }

    private fun retrieveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val productRef: DatabaseReference=database.reference.child("menu")
        //fetch data from database
        productRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear existing data before populating
                menuItems.clear()

                //loop for through each food item
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(AllProduct::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError","Error:${error.message}")
            }
        })
    }
    private fun setAdapter() {
        val adapter =
            MenuItemAdapter(this@AllProductsActivity,menuItems,databaseReference){position ->
                deleteMenuItems(position)
            }
        binding.addProductsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.addProductsRecyclerView.adapter = adapter

    }
    private fun deleteMenuItems(position: Int) {
  val menuItemToDelete = menuItems[position]
        val menuItemKey = menuItemToDelete.key
        val foodMenuReference = database.reference.child("menu").child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener { task->
            if(task.isSuccessful){
                menuItems.removeAt(position)
                binding.addProductsRecyclerView.adapter?.notifyItemRemoved(position)
            }
            else{
                Toast.makeText(this, "Item not Deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


