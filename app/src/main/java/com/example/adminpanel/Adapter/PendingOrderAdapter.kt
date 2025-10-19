package com.example.adminpanel.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminpanel.databinding.PendingorderLayoutBinding

class PendingOrderAdapter(private val context: Context,
                          private val customerNames: MutableList<String>,
                          private val totalPrice: MutableList<String>,
                          private val productImage: MutableList<String>,
                          private val itemClicked:OnItemClicked
               ): RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

 interface OnItemClicked
    {
       fun onItemClickListener(position: Int)
       fun onItemAcceptClickListener(position: Int)
       fun onItemDispatchClickListener(position: Int)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderAdapter.PendingOrderViewHolder {
        val binding = PendingorderLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderAdapter.PendingOrderViewHolder,position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size
    inner class PendingOrderViewHolder(private  val binding: PendingorderLayoutBinding):RecyclerView.ViewHolder(binding.root){
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                quantityTextView.text = totalPrice[position]
                var uriString = productImage[position]
                var uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(productImages)

                orderAcceptButton.apply {
                    if(!isAccepted){
                        text = "Accept"
                    }
                    else{
                        text = "Dispatch"
                    }
                    setOnClickListener{
                        if(!isAccepted){
                            text = "Dispatch"
                            isAccepted = true
                            showToast("Order is accepted")
                            itemClicked.onItemAcceptClickListener(position)

                        }
                        else{
                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Oder is Dispatched")
                            itemClicked.onItemDispatchClickListener(position)
                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClickListener(position)
                }
            }
        }
        fun showToast(message: String){
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
        }
    }
}