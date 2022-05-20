package com.example.pedimearequito.activities.client.adress.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.client.adress.create.ClientAdressCreateActivity
import com.example.pedimearequito.activities.client.payments.form.ClientPaymentsFormActivity
import com.example.pedimearequito.activities.client.payments.payment_method.ClientPaymentMethodActivity
import com.example.pedimearequito.adapters.AddressAdapter
import com.example.pedimearequito.adapters.ShoppingBagAdapter
import com.example.pedimearequito.models.*
import com.example.pedimearequito.providers.AddressProvider
import com.example.pedimearequito.providers.OrdersProvider
import com.example.pedimearequito.utils.SharedPref

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAdressListActivity : AppCompatActivity() {

    var fabCreateAdress: FloatingActionButton? = null
    var toolbar: Toolbar? = null

    var recyclerViewAddress: RecyclerView? = null
    var adapter: AddressAdapter? = null
    var addressProvider: AddressProvider? = null
    var ordersProvider: OrdersProvider? = null
    var sharedPref: SharedPref? = null
    var user: User? = null

    var address= ArrayList<Address>()
    val gson= Gson()

    var buttonNext: Button? = null

    var selectedProducts= ArrayList<Product>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_adress_list)

        sharedPref = SharedPref(this)
        getProductsFromSharedPref()

        recyclerViewAddress = findViewById(R.id.recyclerview_address)


        recyclerViewAddress?.layoutManager = LinearLayoutManager(this)
        buttonNext = findViewById(R.id.btn_next)
        fabCreateAdress= findViewById(R.id.fab_adress_create)

        toolbar=findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        toolbar?.title= "Mis direcciones"
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getUserFromSession()

        addressProvider= AddressProvider(user?.sessionToken!!)
        ordersProvider= OrdersProvider(user?.sessionToken!!)

        fabCreateAdress?.setOnClickListener {  goToAdressCreate()}

        getAddress()
        buttonNext?.setOnClickListener { getAddressFromSession() }
    }

    private fun getProductsFromSharedPref() {
        if (!sharedPref?.getdata("order").isNullOrBlank()) { //VALIDAMOS SI EXITE UNA ORDEN
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            selectedProducts = gson.fromJson(sharedPref?.getdata("order"), type)


        }
    }

    private fun createOrder(idAddress: String) {
        goToPaymentsForm()
//        val order= Order(
//            products = selectedProducts,
//            idClient = user?.id!!,
//            idAddress = idAddress
//
//        )
//        ordersProvider?.create(order)?.enqueue(object: Callback<ResponseHttp>{
//            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
//
//                if (response.body() != null){ //SI HUBO RSPUESTA DEL SERVIDOR ENTONCES
//
//                    Toast.makeText(this@ClientAdressListActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()
//                }
//                else{
//                    Toast.makeText(this@ClientAdressListActivity, "Ocurrio un error en la peticion", Toast.LENGTH_LONG).show()
//
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
//                Toast.makeText(this@ClientAdressListActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
//            }
//
//        } )
    }
    private fun getAddressFromSession(){
        if (!sharedPref?.getdata("address").isNullOrBlank()){
            val a = gson.fromJson(sharedPref?.getdata("address"), Address::class.java)//si existe direccion lo enviamos a otro form
           createOrder(a.id!!)
        // goToPaymentsForm()
        }
        else {
            Toast.makeText(this, "Selecciona una dirección para continuar", Toast.LENGTH_LONG).show()
        }
    }
    private fun goToPaymentsForm(){
        val i= Intent(this, ClientPaymentMethodActivity::class.java )
        startActivity(i)
    }
    fun resetValue(position: Int) {
        val viewHolder = recyclerViewAddress?.findViewHolderForAdapterPosition(position)//OBTENEMOS DIRECCION
        val view = viewHolder?.itemView
        val imageViewCheck= view?.findViewById<ImageView>(R.id.imageview_check)
        imageViewCheck?.visibility = View.GONE
    }

    private fun getAddress(){
        addressProvider?.getAddress(user?.id!!)?.enqueue(object : Callback<ArrayList<Address>>{
            override fun onResponse(call: Call<ArrayList<Address>>,response: Response<ArrayList<Address>>) {

               if (response.body() != null){
                address = response.body()!!
                   adapter= AddressAdapter(this@ClientAdressListActivity, address  )
                   recyclerViewAddress?.adapter = adapter
               }
            }

            override fun onFailure(call: Call<ArrayList<Address>>, t: Throwable) {
                Toast.makeText(this@ClientAdressListActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()            }

        })
    }

    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getdata("user").isNullOrBlank()) {
            //SI EL USUARIO EXISTE EN SESION, OBTENGO INFO
            user = gson.fromJson(sharedPref?.getdata("user"), User::class.java)

        }
    }
    private  fun goToAdressCreate(){
        val i = Intent(this, ClientAdressCreateActivity::class.java)
        startActivity(i)
    }
}