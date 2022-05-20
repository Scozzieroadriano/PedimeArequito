package com.example.pedimearequito.activities.comercio.orders.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.comercio.home.RestaurantHomeActivity
import com.example.pedimearequito.adapters.OrderProductsAdapter

import com.example.pedimearequito.models.Order
import com.example.pedimearequito.models.ResponseHttp
import com.example.pedimearequito.models.User
import com.example.pedimearequito.providers.OrdersProvider
import com.example.pedimearequito.providers.UsersProvider
import com.example.pedimearequito.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ComercioOrdersDetailActivity : AppCompatActivity() {
    val TAG = "ClientOrdersDetail"
    var order: Order?= null
    val gson= Gson()

    var toolbar: Toolbar? = null
    var textViewClient: TextView? = null
    var textViewAddress: TextView? = null
    var textViewDate: TextView? = null
    var textViewTotal: TextView? = null
    var textViewStatus: TextView? = null
    var textviewDeliveryAvailable:TextView? = null
    var recyclerViewProducts: RecyclerView? = null
    var buttonUpdate: Button? = null
    var textViewDelivery: TextView? = null
    var textViewDeliveryAssigned: TextView? = null

    var adapter: OrderProductsAdapter?= null

    var ordersProvider: OrdersProvider?= null
    var usersProvider: UsersProvider? = null
    var user: User? = null
    var sharedPref: SharedPref? = null
    var spinnerDeliveryMan: Spinner? = null

    var idDelivery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comercio_orders_detail)

        sharedPref = SharedPref(this)

        order= gson.fromJson(intent.getStringExtra("order"),Order::class.java)
        getUserFromSession()

        usersProvider= UsersProvider(user?.sessionToken!!)
        ordersProvider= OrdersProvider(user?.sessionToken!!)

        toolbar= findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.black))
        toolbar?.title="Orden #${order?.id!!}"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textViewClient = findViewById(R.id.textview_client)
        textViewAddress = findViewById(R.id.textview_address)
        textViewDate = findViewById(R.id.textview_date)
        textViewTotal = findViewById(R.id.textview_total)
        textViewStatus = findViewById(R.id.textview_status)
        textviewDeliveryAvailable = findViewById(R.id.txv_spiner_repartidores)
        textViewDelivery = findViewById(R.id.textview_deliveryman)
        textViewDeliveryAssigned= findViewById((R.id.textview_delivery_assigned))

        spinnerDeliveryMan = findViewById(R.id.spinner_delivery_men)
        buttonUpdate= findViewById(R.id.btn_update_order)

        recyclerViewProducts = findViewById(R.id.recyclerview_products)
        recyclerViewProducts?.layoutManager = LinearLayoutManager (this)

        adapter = OrderProductsAdapter(this,order?.products!!)
        recyclerViewProducts?.adapter = adapter

        textViewClient?.text = "${order?.client?.name}${" "}${order?.client?.lastname}"
        textViewAddress?.text = order?.address?.address
        textViewDate?.text = "${order?.timestamp}"
        textViewStatus?.text = order?.status
        textViewDelivery?.text = "${order?.delivery?.name} ${order?.delivery?.lastname}"

        Log.d(TAG, "Orden: ${order.toString()}")

        getTotal()
        getDeliveryMan()

        if(order?.status == "PAGADO") {
            buttonUpdate?.visibility= View.VISIBLE
            textviewDeliveryAvailable?.visibility= View.VISIBLE
            spinnerDeliveryMan?.visibility= View.VISIBLE
        }
        if(order?.status != "PAGADO") {
            textViewDeliveryAssigned?.visibility = View.VISIBLE
            textViewDelivery?.visibility = View.VISIBLE
        }

            buttonUpdate?.setOnClickListener { updateOrder() }
    }
    private fun updateOrder(){
        order?.idDelivery = idDelivery

        ordersProvider?.updateToDispatched(order!!)?.enqueue(object: Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                if (response.body() != null ){


                     if (response.body()?.isSuccess == true){
                    Toast.makeText(this@ComercioOrdersDetailActivity, "Repartidor asignado correctamente", Toast.LENGTH_LONG).show()
                     goToOrders()
                }else {
                    Toast.makeText(this@ComercioOrdersDetailActivity, "No se pudo asignar el repartidor", Toast.LENGTH_LONG).show()

                }

                }
                else {
                    Toast.makeText(this@ComercioOrdersDetailActivity, "No hubo respuesta del servidor", Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@ComercioOrdersDetailActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }
    private fun goToOrders(){
        val i = Intent(this, RestaurantHomeActivity::class.java)
        i.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }

    private fun getDeliveryMan() {
        usersProvider?.getDeliveryMan()?.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>>,response: Response<ArrayList<User>>) {

                if (response.body() != null) {
                    val deliveryMan= response.body() // le asigno la lista de repartidores

    val arrayAdapter = ArrayAdapter<User>(this@ComercioOrdersDetailActivity,android.R.layout.simple_dropdown_item_1line,deliveryMan!!
        )
        spinnerDeliveryMan?.adapter = arrayAdapter

        spinnerDeliveryMan?.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                l: Long
                            ) {
                                idDelivery = deliveryMan[position].id!! //ACA SELECCIONAMOS DEL SPINER, EL ID DEL DELIVERY
                                Log.d(TAG, "Id Delivery: $idDelivery")
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                               TODO("Not yet implemented")
                            }
                     }
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Toast.makeText(this@ComercioOrdersDetailActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getdata("user").isNullOrBlank()) {
            //SI EL USUARIO EXISTE EN SESION, OBTENGO INFO
            user = gson.fromJson(sharedPref?.getdata("user"), User::class.java)
        }
    }

    private  fun getTotal(){
        var total = 0.0
        for (p in order?.products!!) {
            total= total + (p.price * p.quantity!!)
            textViewTotal?.text = "${"$" } ${total}"
        }

    }
}