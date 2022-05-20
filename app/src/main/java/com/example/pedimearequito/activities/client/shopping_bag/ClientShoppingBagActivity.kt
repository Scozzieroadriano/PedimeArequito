package com.example.pedimearequito.activities.client.shopping_bag

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.client.adress.list.ClientAdressListActivity
import com.example.pedimearequito.adapters.ShoppingBagAdapter
import com.example.pedimearequito.models.Product
import com.example.pedimearequito.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ClientShoppingBagActivity : AppCompatActivity() {

    var recyclerViewShoppingBag: RecyclerView? = null
    var texViewTotal: TextView? = null
    var buttonNext:Button? = null
    var toolbar: Toolbar? =  null

    var adapter: ShoppingBagAdapter? = null
    var sharedPref: SharedPref? = null
    var gson = Gson()
    var selectedProducts = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_shopping_bag)

        sharedPref = SharedPref(this)

        recyclerViewShoppingBag = findViewById(R.id.recyclerview_shopping_bag)
        texViewTotal = findViewById(R.id.textview_total)
        buttonNext = findViewById(R.id.btn_accept)
        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        toolbar?.title = "Tu Carrito de Compras"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerViewShoppingBag?.layoutManager = LinearLayoutManager (this)

        getProductsFromSharedPref()

        buttonNext?.setOnClickListener { goToAdressList() }
    }

    private fun goToAdressList (){
        val i = Intent(this, ClientAdressListActivity::class.java)
        startActivity(i)
    }
    fun setTotal(total: Double) {
        texViewTotal?.text = "${"$ "}${total}"
    }

    private fun getProductsFromSharedPref() {
        if (!sharedPref?.getdata("order").isNullOrBlank()) { //VALIDAMOS SI EXITE UNA ORDEN
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            selectedProducts = gson.fromJson(sharedPref?.getdata("order"), type)

            adapter= ShoppingBagAdapter(this, selectedProducts)
            recyclerViewShoppingBag?.adapter = adapter
        }
    }
}