package com.example.pedimearequito.activities.delivery.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.MainActivity

import com.example.pedimearequito.fragments.client.ClientOrdersFragment
import com.example.pedimearequito.fragments.client.ClientProflileFragment
import com.example.pedimearequito.fragments.comercio.ComercioCreateCategoriFragment
import com.example.pedimearequito.fragments.comercio.ComercioOrdersFragment
import com.example.pedimearequito.fragments.comercio.RestaurantProductFragment
import com.example.pedimearequito.fragments.delivery.DeliveryOrdersFragment
import com.example.pedimearequito.models.User
import com.example.pedimearequito.utils.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class DeliveryHomeActivity : AppCompatActivity() {

    private  val TAG= "DeliveryHomeActivity"
    //var buttonLogout: Button? = null
    var sharedPref: SharedPref? = null

    var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_home)

        sharedPref= SharedPref(this)

        //buttonLogout = findViewById(R.id.btn_logout)
        //buttonLogout?.setOnClickListener { }

        openFragment(DeliveryOrdersFragment())

        bottomNavigation = findViewById(R.id.bottomnavigation)
        bottomNavigation?.setOnItemSelectedListener {


            when (it.itemId) {
                R.id.item_orders -> {
                    openFragment(DeliveryOrdersFragment())
                    true
                }

                R.id.item_profile -> {
                    openFragment(ClientProflileFragment())
                    true
                }
                else -> false
            }
        }

        getUserFromSession()
    }
    private fun openFragment (fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun logout(){
        sharedPref?.remove("user")
        val i= Intent(this, MainActivity::class.java)
        startActivity(i)
    }
    private  fun getUserFromSession(){


        val gson= Gson()

        if (!sharedPref?.getdata("user").isNullOrBlank()){
            //SI EL USUARIO EXISTE EN SESION, OBTENGO INFO
            val user= gson.fromJson(sharedPref?.getdata("user"), User::class.java)
            Log.d(TAG, "Usuario: $user")
        }

    }
}