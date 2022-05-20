package com.example.pedimearequito.activities.client.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.pedimearequito.R
import com.example.pedimearequito.fragments.client.ClientListComerciosFragment
import com.example.pedimearequito.fragments.client.ClientOrdersFragment
import com.example.pedimearequito.fragments.client.ClientProflileFragment
import com.example.pedimearequito.models.Category
import com.example.pedimearequito.models.User
import com.example.pedimearequito.providers.UsersProvider
import com.example.pedimearequito.utils.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import java.nio.file.attribute.UserPrincipal

class ClientHomeActivity : AppCompatActivity() {

    private  val TAG= "ClientHomeActivity"
    //var buttonLogout: Button? = null
    var sharedPref: SharedPref? = null

    var bottomNavigation: BottomNavigationView? = null
    var userProvider: UsersProvider? = null
    var user: User? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_home)

        sharedPref= SharedPref(this)





        openFragment(ClientListComerciosFragment())
        bottomNavigation = findViewById(R.id.bottomnavigation)

        bottomNavigation?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> {
                    openFragment(ClientListComerciosFragment())
                    true
                }
                R.id.item_orders -> {
                    openFragment(ClientOrdersFragment())
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

        userProvider = UsersProvider(token = user?.sessionToken!!)
        createToken()
    }
    private fun createToken(){
        userProvider?.createToken(user!!, this)

    }
    private fun openFragment (fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private  fun getUserFromSession(){


        val gson= Gson()

        if (!sharedPref?.getdata("user").isNullOrBlank()){
            //SI EL USUARIO EXISTE EN SESION, OBTENGO INFO
             user= gson.fromJson(sharedPref?.getdata("user"), User::class.java)

        }

    }
}