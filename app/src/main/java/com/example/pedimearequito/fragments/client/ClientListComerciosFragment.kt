package com.example.pedimearequito.fragments.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.client.shopping_bag.ClientShoppingBagActivity
import com.example.pedimearequito.adapters.CommerceAdapter
import com.example.pedimearequito.models.User
import com.example.pedimearequito.providers.UsersProvider
import com.example.pedimearequito.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ClientListComerciosFragment : Fragment() {
    val TAG = "ClientCommerce"
    var myView: View? = null
    var recyclerViewListCommerce: RecyclerView? = null
    var userProviders: UsersProvider? = null
    var adapter : CommerceAdapter? = null
    var user: User? = null
    var sharedPref: SharedPref? = null
    var users= ArrayList<User>()
    


    var toolbar: Toolbar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_list_comercios, container, false)

        setHasOptionsMenu(true)
        toolbar = myView?.findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        toolbar?.title = "Comercios/Almacenes"

        (activity as AppCompatActivity).setSupportActionBar(toolbar)



        recyclerViewListCommerce = myView?.findViewById(R.id.recyclerview_commerce)
        recyclerViewListCommerce?.layoutManager = LinearLayoutManager(requireContext()) //mostrar elementos de manera vertical
        sharedPref = SharedPref(requireActivity())
        getUserFromSession()

        userProviders = UsersProvider(user?.sessionToken!!)

        getCommerces()

        return myView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shopping_bag, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_shoping_bag) {
            goToShoppingBag()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun goToShoppingBag(){
        val i = Intent(requireContext(), ClientShoppingBagActivity::class.java)
        startActivity(i)
    }
    private fun getCommerces(){
        userProviders?.getClientCommerce()?.enqueue(object: Callback<ArrayList<User>>{
            override fun onResponse(call: Call<ArrayList<User>>,response: Response<ArrayList<User>>
            ) {
                if (response.body() != null) {

                    users = response.body()!!
                    adapter = CommerceAdapter(requireActivity(), users)
                    recyclerViewListCommerce?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}" )
                Toast.makeText(requireContext(), "ERROR: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private  fun getUserFromSession(){

        val gson= Gson()

        if (!sharedPref?.getdata("user").isNullOrBlank()){
            //SI EL USUARIO EXISTE EN SESION, OBTENGO INFO
            user= gson.fromJson(sharedPref?.getdata("user"), User::class.java)

        }

    }
}

