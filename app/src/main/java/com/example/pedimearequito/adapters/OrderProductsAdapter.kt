package com.example.pedimearequito.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pedimearequito.R
import com.example.pedimearequito.models.Product
import com.example.pedimearequito.utils.SharedPref

class OrderProductsAdapter (val context: Activity, val products: ArrayList <Product>):RecyclerView.Adapter<OrderProductsAdapter.OrderProductsViewHolder>() {

    val sharedPref = SharedPref (context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_order_products,parent,false)
        return OrderProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: OrderProductsViewHolder, position: Int) {

        val product = products[position] //DEVUELVE CADA CATEGORIA

        holder.textViewName.text = product.name

        if(product.quantity != null) {
            holder.textViewQuantity.text = "${product.quantity!!}"
        }
        Glide.with(context).load(product.image1).into(holder.imageViewProduct)

        }

    class OrderProductsViewHolder (view: View): RecyclerView.ViewHolder(view) {

        val imageViewProduct: ImageView
        val textViewName: TextView
        val textViewQuantity: TextView

        init {
            imageViewProduct= view.findViewById(R.id.imageview_product_order)
            textViewName= view.findViewById(R.id.texview_name_order_product)
            textViewQuantity= view.findViewById(R.id.textView_quantity)
        }
    }
}