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
import com.example.pedimearequito.activities.client.products.list.ClientProductListActivity
import com.example.pedimearequito.activities.comercio.categories.CategoriesCommerceListActivity
import com.example.pedimearequito.models.Category
import com.example.pedimearequito.models.User
import com.example.pedimearequito.utils.SharedPref

class CommerceAdapter (val context: Activity, val users: ArrayList <User>):RecyclerView.Adapter<CommerceAdapter.CommerceViewHolder>() {

    val sharedPref = SharedPref (context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommerceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_commerces,parent,false)
        return CommerceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: CommerceViewHolder, position: Int) {

        val user = users[position] //DEVUELVE CADA COMERCIO

        holder.textViewCommerce.text = user.name
        Glide.with(context).load(user.image).into(holder.imageViewCommerce)

      holder.itemView.setOnClickListener { goToCategories(user) }
   }

   private fun goToCategories(user: User) {
       val i = Intent(context, CategoriesCommerceListActivity::class.java)
       i.putExtra("idUser", user.id )
      context.startActivity(i)
   }

    class CommerceViewHolder (view: View): RecyclerView.ViewHolder(view) {

        val textViewCommerce: TextView
        val imageViewCommerce: ImageView

        init {
            textViewCommerce= view.findViewById(R.id.textview_name_commerce)
            imageViewCommerce= view.findViewById(R.id.imageview_commerce)
        }

    }
}