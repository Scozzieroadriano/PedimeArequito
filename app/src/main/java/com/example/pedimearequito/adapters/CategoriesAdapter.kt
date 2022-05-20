package com.example.pedimearequito.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.client.products.list.ClientProductListActivity import com.example.pedimearequito.models.Category
import com.example.pedimearequito.utils.SharedPref

class CategoriesAdapter (val context: Activity, val categories: ArrayList <Category>):RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    val sharedPref = SharedPref (context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_categories,parent,false)
        return CategoriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {

        val category = categories[position] //DEVUELVE CADA CATEGORIA

        holder.textViewCategory.text = category.name
        Glide.with(context).load(category.image).into(holder.imageViewCategory)

       holder.itemView.setOnClickListener { goToProducts(category) }
    }

   private fun goToProducts(category: Category) {
       val i = Intent(context, ClientProductListActivity::class.java)
       i.putExtra("idCategory", category.id)
      context.startActivity(i)
   }


    class CategoriesViewHolder (view: View): RecyclerView.ViewHolder(view) {

        val textViewCategory: TextView
        val imageViewCategory: ImageView

        init {
            textViewCategory= view.findViewById(R.id.texview_category)
            imageViewCategory= view.findViewById(R.id.imageview_category)
        }

    }
}