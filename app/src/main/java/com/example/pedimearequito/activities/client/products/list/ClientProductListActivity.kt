package com.example.pedimearequito.activities.client.products.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pedimearequito.R
import com.example.pedimearequito.adapters.ProductsAdapter
import com.example.pedimearequito.models.Product
import com.example.pedimearequito.models.User
import com.example.pedimearequito.providers.ProductsProvider
import com.example.pedimearequito.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientProductListActivity : AppCompatActivity() {

    val TAG = "ClientProducts"
    var recyclerViewProducts: RecyclerView? = null
    var adapter: ProductsAdapter? = null
    var user: User? = null
    var productsProviders: ProductsProvider? = null
    var products: ArrayList<Product> = ArrayList()
    var sharedPref: SharedPref? = null
    var idCategory: String? = null

    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_product_list)
        sharedPref = SharedPref(this)
        idCategory = intent.getStringExtra("idCategory")

        getUserFromSession()
        productsProviders = ProductsProvider(user?.sessionToken!!)

        recyclerViewProducts = findViewById(R.id.recyclerview_products)
        recyclerViewProducts?.layoutManager = GridLayoutManager(this, 2)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        toolbar?.title = "Productos"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getProducts()

    }

    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getdata("user").isNullOrBlank()) {
            //SI EL USUARIO EXISTE EN SESION, OBTENGO INFO
            user = gson.fromJson(sharedPref?.getdata("user"), User::class.java)

        }
    }

    private fun getProducts() {
        productsProviders?.findByCategory(idCategory!!)
            ?.enqueue(object : Callback<ArrayList<Product>> {
                override fun onResponse(
                    call: Call<ArrayList<Product>>, response: Response<ArrayList<Product>>
                ) {
                    if (response.body() != null) {
                        products = response.body()!!
                        adapter = ProductsAdapter(this@ClientProductListActivity, products)
                        recyclerViewProducts?.adapter = adapter
                    }


                }

                override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                    Toast.makeText(this@ClientProductListActivity, t.message, Toast.LENGTH_LONG)
                        .show()
                    Log.d(TAG, "Error: ${t.message}")
                }

            })
    }
}