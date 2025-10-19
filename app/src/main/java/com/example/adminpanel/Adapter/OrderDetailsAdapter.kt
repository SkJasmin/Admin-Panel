package com.example.adminpanel.Adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.example.adminpanel.databinding.OrderDetailsItemsBinding

class OrderDetailsAdapter(private var context: Context,
    private var productNames: ArrayList<String>,
    private var productImages: ArrayList<String>,
    private var productQuantities:ArrayList<Int>,
    private var productPrices:ArrayList<String>
) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderDetailsViewHolder {
        val binding = OrderDetailsItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OrderDetailsViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int  = productNames.size

    inner class OrderDetailsViewHolder(private  val binding: OrderDetailsItemsBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
binding.apply {
    productName.text = productNames[position]
    val uriString = productImages[position]
    val uri = Uri.parse(uriString)
    Glide.with(context).load(uri).into(productImage)
    productQuantity.text = productQuantities[position].toString()
    productPrice.text = productPrices[position]


}
        }
    }
}


