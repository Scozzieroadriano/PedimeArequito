package com.example.pedimearequito.activities.comercio.categories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pedimearequito.R
import com.example.pedimearequito.adapters.CategoriesAdapter
import com.example.pedimearequito.models.Category
import com.example.pedimearequito.models.User
import com.example.pedimearequito.providers.CategoriesProvider
import com.example.pedimearequito.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesCommerceListActivity : AppCompatActivity() {
    val TAG = "LISTCOMMERCE"
    var recyclerViewCategories: RecyclerView? = null
    var adapter: CategoriesAdapter? = null
    var user: User? = null
    var categoriesProvider: CategoriesProvider? = null

    var sharedPref: SharedPref? = null

    var toolbar: Toolbar? = null

    var idUser: String? = null
    var categories: ArrayList<Category> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_commerce_list)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        toolbar?.title = "Lista de Categorías"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPref = SharedPref(this)

        idUser = intent.getStringExtra("idUser")
        getUserFromSession()
        categoriesProvider = CategoriesProvider(user?.sessionToken!!)

        recyclerViewCategories = findViewById(R.id.recyclerview_categories)
        recyclerViewCategories?.layoutManager = GridLayoutManager(this, 2)

        getCategories()
    }
   private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getdata("user").isNullOrBlank()) {
            //SI EL USUARIO EXISTE EN SESION, OBTENGO INFO
            user = gson.fromJson(sharedPref?.getdata("user"), User::class.java)

        }


    }

    private fun getCategories() {
        categoriesProvider?.getByIdUser(idUser!!)?.enqueue(object : Callback<ArrayList<Category>>{
            override fun onResponse(call: Call<ArrayList<Category>>,response: Response<ArrayList<Category>>
            ) {

                if(response.body() != null) {
                    categories = response.body()!!
                    adapter = CategoriesAdapter(this@CategoriesCommerceListActivity, categories)
                    recyclerViewCategories?.adapter = adapter

                }


            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Toast.makeText(this@CategoriesCommerceListActivity, t.message, Toast.LENGTH_LONG).show()
                Log.d(TAG, "ERROR: ${t.message}")
            }

        })
    }
}