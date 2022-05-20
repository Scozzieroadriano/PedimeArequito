package com.example.pedimearequito.activities.comercio.products

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import com.example.pedimearequito.R
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pedimearequito.adapters.ListProductsAdapter
import com.example.pedimearequito.models.Product
import com.example.pedimearequito.models.User
import com.example.pedimearequito.providers.ProductsProvider
import com.example.pedimearequito.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyListProductActivity : AppCompatActivity(){

    val TAG = "ListProducts"
    var recyclerViewProducts: RecyclerView? = null
    var adapter: ListProductsAdapter ? = null
    var user: User? = null
    var productsProviders: ProductsProvider? = null
    var products: ArrayList<Product> = ArrayList()
    var sharedPref: SharedPref? = null
    var idUser: String? = null

    var toolbar: Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list_product)
        sharedPref = SharedPref(this)
        idUser = intent.getStringExtra("idUser")

        getUserFromSession()
        productsProviders = ProductsProvider(user?.sessionToken!!)

        recyclerViewProducts = findViewById(R.id.recyclerview_mylistproducts)
        recyclerViewProducts?.layoutManager = LinearLayoutManager(this)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        toolbar?.title = "Mis Productos"

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
        productsProviders?.findByIdUser(idUser!!)
            ?.enqueue(object : Callback<ArrayList<Product>> {
                override fun onResponse(
                    call: Call<ArrayList<Product>>, response: Response<ArrayList<Product>>
                ) {
                    if (response.body() != null) {
                        products = response.body()!!
                        adapter = ListProductsAdapter(this@MyListProductActivity, products)
                        recyclerViewProducts?.adapter = adapter
                    }



                }

                override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                    Toast.makeText(this@MyListProductActivity, t.message, Toast.LENGTH_LONG)
                        .show()
                    Log.d(TAG, "Error: ${t.message}")
                }

            })
    }


}