package com.example.pedimearequito.fragments.comercio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pedimearequito.R
import com.example.pedimearequito.adapters.OrdersClientAdapter
import com.example.pedimearequito.adapters.OrdersComercioAdapter
import com.example.pedimearequito.models.Order
import com.example.pedimearequito.models.User
import com.example.pedimearequito.providers.OrdersProvider
import com.example.pedimearequito.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ComercioOrdersStatusFragment : Fragment() {

    var myView: View? = null
    var ordersProvider: OrdersProvider? = null
    var user: User? = null
    var sharedPref: SharedPref? = null

    var recyclerViewOrders: RecyclerView? = null
    var adapter: OrdersComercioAdapter? = null

    var status = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_comercio_orders_status, container, false)

        sharedPref= SharedPref(requireActivity())

        status = arguments?.getString("status")!!

        getUserFromSession()
        ordersProvider = OrdersProvider(user?.sessionToken!!)

        recyclerViewOrders = myView?.findViewById(R.id.recyclerview_orders)
        recyclerViewOrders?.layoutManager = LinearLayoutManager(requireContext())

        getOrders()

        return myView
    }

    private fun getOrders() {
        ordersProvider?.getOrdersByStatus(status)?.enqueue(object: Callback<ArrayList<Order>>{
            override fun onResponse(call: Call<ArrayList<Order>>, response: Response<ArrayList<Order>>) {

                if (response.body() != null) {

                    val orders = response.body()
                    adapter = OrdersComercioAdapter(requireActivity(),orders!!)
                    recyclerViewOrders?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Order>>, t: Throwable) {
                Toast.makeText(requireActivity(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
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
}