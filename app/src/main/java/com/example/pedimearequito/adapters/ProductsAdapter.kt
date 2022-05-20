package com.example.pedimearequito.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.client.products.detail.ClientProductsDetailActivity
import com.example.pedimearequito.models.Product
import com.example.pedimearequito.utils.SharedPref

class ProductsAdapter (val context: Activity, val products: ArrayList <Product>):RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    val sharedPref = SharedPref (context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_product,parent,false)
        return ProductsViewHolder(view)

    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {

        val product = products[position] //DEVUELVE CADA CATEGORIA

        holder.textViewName.text = product.name
        holder.textViewPrice.text = "${"$ "}${product.price}"
        holder.textViewquantity.text = "${"Cantidad: "}${product.stock}"


        Glide.with(context).load(product.image1).into(holder.imageViewProduct)

        holder.itemView.setOnClickListener { goToDetail(product) }
    }


   private fun goToDetail(product: Product) {
       val i = Intent(context, ClientProductsDetailActivity::class.java)
       i.putExtra("product", product.toJson())
        context.startActivity(i)
    }

    class ProductsViewHolder (view: View): RecyclerView.ViewHolder(view) {

        val textViewName: TextView
        val textViewPrice: TextView
        val textViewquantity: TextView
        val imageViewProduct: ImageView


        init {
            textViewName= view.findViewById(R.id.textview_name)
            textViewPrice= view.findViewById(R.id.textview_price)
            textViewquantity= view.findViewById(R.id.textView_quantity)
            imageViewProduct= view.findViewById(R.id.imageview_product)
        }

    }
}