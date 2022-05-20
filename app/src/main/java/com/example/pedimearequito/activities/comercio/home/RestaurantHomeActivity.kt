package com.example.pedimearequito.activities.comercio.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.MainActivity
import com.example.pedimearequito.fragments.client.ClientProflileFragment
import com.example.pedimearequito.fragments.comercio.ComercioCreateCategoriFragment
import com.example.pedimearequito.fragments.comercio.ComercioOrdersFragment
import com.example.pedimearequito.fragments.comercio.RestaurantProductFragment
import com.example.pedimearequito.models.User
import com.example.pedimearequito.utils.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class RestaurantHomeActivity : AppCompatActivity() {

    private  val TAG= "RestaurantHomeActivity"
    //var buttonLogout: Button? = null
    var sharedPref: SharedPref? = null

    var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_home)

        sharedPref= SharedPref(this)


        openFragment(ComercioOrdersFragment())

        bottomNavigation = findViewById(R.id.bottomnavigation)
        bottomNavigation?.setOnItemSelectedListener {


            when (it.itemId) {
                R.id.item_home -> {
                    openFragment(ComercioOrdersFragment())
                    true
                }
                R.id.item_category -> {
                    openFragment(ComercioCreateCategoriFragment())
                    true
                }
                R.id.item_product -> {
                    openFragment(RestaurantProductFragment())
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