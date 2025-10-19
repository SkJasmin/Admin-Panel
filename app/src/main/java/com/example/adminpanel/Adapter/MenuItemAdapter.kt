package com.example.adminpanel.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminpanel.databinding.ProductLayoutBinding
import com.example.adminpanel.model.AllProduct
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter (
    private val context: Context,
    private val menuList: ArrayList<AllProduct>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener:(position:Int)->Unit
) : RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {

        private val itemQuantities = IntArray(menuList.size) { 1 } // Default quantity = 1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
            val binding = ProductLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return AddItemViewHolder(binding)
        }

        override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
            holder.bind(position)
        }

        override fun getItemCount(): Int = menuList.size

        inner class AddItemViewHolder(private val binding: ProductLayoutBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(position: Int) {
                binding.apply {
                    val quantity = itemQuantities[position]
                    val menuItem=menuList[position]
                    val uriString=menuItem.productImage
                    val uri= Uri.parse(uriString)
                    productTextView.text = menuItem.productName
                    productPriceTextView.text = menuItem.productPrice
                    Glide.with(context).load(uri).into(productImageView)
                    quantityTextView.text = quantity.toString()

                    // Handle button clicks
                    minusButton.setOnClickListener {
                        decreaseQuantity(position)
                    }
                    plusButton.setOnClickListener {
                        increaseQuantity(position)
                    }
                    deleteButton.setOnClickListener {
                        onDeleteClickListener(position)
                    }
                }
            }

            private fun decreaseQuantity(position: Int) {
                if (itemQuantities[position] > 1) {
                    itemQuantities[position]--
                    binding.quantityTextView.text = itemQuantities[position].toString()
                }
            }

            private fun increaseQuantity(position: Int) {
                if (itemQuantities[position] < 10) { // Set maximum quantity to 10
                    itemQuantities[position]++
                    binding.quantityTextView.text = itemQuantities[position].toString()
                }
            }

            private fun deleteItem(position: Int) {
                // Remove the item from all lists
                menuList.removeAt(position)
                menuList.removeAt(position)
                menuList.removeAt(position)

                // Notify adapter about the removed item
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, menuList.size)
            }
        }
    }

