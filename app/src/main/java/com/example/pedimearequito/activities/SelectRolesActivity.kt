package com.example.pedimearequito.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pedimearequito.R
import com.example.pedimearequito.adapters.RolesAdapter
import com.example.pedimearequito.models.User
import com.example.pedimearequito.utils.SharedPref
import com.google.gson.Gson

class SelectRolesActivity : AppCompatActivity() {

    var recyclerViewRoles: RecyclerView? = null
    var user : User? = null
    var adapter: RolesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_roles)

        recyclerViewRoles= findViewById(R.id.recyclerview_roles)
        recyclerViewRoles?.layoutManager = LinearLayoutManager(this)

        getUserFromSession()

        adapter = RolesAdapter(this,user?.roles!!)

        recyclerViewRoles?.adapter = adapter
    }

    private  fun getUserFromSession(){

        val sharedPref = SharedPref(this)
        val gson= Gson()

        if (!sharedPref.getdata("user").isNullOrBlank()){
            //SI EL USUARIO EXISTE EN SESION, OBTENGO INFO
            user= gson.fromJson(sharedPref.getdata("user"), User::class.java)

        }

    }
}