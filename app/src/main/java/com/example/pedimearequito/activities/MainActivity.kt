package com.example.pedimearequito.activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.client.home.ClientHomeActivity
import com.example.pedimearequito.activities.delivery.home.DeliveryHomeActivity
import com.example.pedimearequito.activities.comercio.home.RestaurantHomeActivity
import com.example.pedimearequito.models.Category
import com.example.pedimearequito.models.ResponseHttp
import com.example.pedimearequito.models.User
import com.example.pedimearequito.providers.UsersProvider
import com.example.pedimearequito.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var imageViewGoToRegister: ImageView? = null
    var editTextEmail: EditText? = null
    var editTextPassword: EditText? = null
    var buttonLogin: Button? = null
    var usersProvider = UsersProvider()




    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_PedimeArequito)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageViewGoToRegister = findViewById(R.id.image_to_register)
        editTextEmail = findViewById(R.id.edittext_email)
        editTextPassword = findViewById(R.id.edittext_password)
        buttonLogin = findViewById(R.id.bt_login)

        imageViewGoToRegister?.setOnClickListener { goToRegister() }

        buttonLogin?.setOnClickListener{ login() }


        getUserFromSession()
    }
    private fun login (){

        val email = editTextEmail?.text.toString()
        val password = editTextPassword?.text.toString()

        if (isValidForm(email, password )) {

            usersProvider.login(email, password)?.enqueue(object : Callback<ResponseHttp> {

                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {

                    Log.d("MainActivity", "Response: ${response.body()}")

                    if (response.body()?.isSuccess == true) {

                        saveUserInSession(response.body()?.data.toString())
                        saveCategoryInSession(response.body()?.data.toString())

                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(this@MainActivity,"Los datos no son correctos",Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(p0: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@MainActivity,"Hubo un error ${t.message}",Toast.LENGTH_LONG).show()
                }

            })
        }
        else {

            Toast.makeText(this,"El formulario no es válido", Toast.LENGTH_LONG).show()
        }

    }

    private fun goToClientHome(){
        val i= Intent(this, ClientHomeActivity::class.java)
        i.flags= FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //eliminar historial de pantalla
        startActivity(i)

    }
    private fun goToRestaurantHome(){

        val i= Intent(this, RestaurantHomeActivity::class.java)
        i.flags= FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)

    }
    private fun goToDeliveryHome(){
        val i= Intent(this, DeliveryHomeActivity::class.java)
        i.flags= FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)

    }
    private fun goToSelectRoles(){
        val i= Intent(this, SelectRolesActivity::class.java)
        i.flags= FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)

    }

    private fun saveUserInSession(data: String) {
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)

        if (user.roles?.size!! > 1) { //TIENE MAS DE UN ROL
            goToSelectRoles()
        } else {
            goToClientHome()
        }

    }
    private fun saveCategoryInSession(data: String) {
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val categ = gson.fromJson(data, Category::class.java)
        sharedPref.save("categ", categ)


    }


    fun String.isEmailValid(): Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private  fun getUserFromSession(){

        val sharedPref = SharedPref(this)
        val gson= Gson()

        if (!sharedPref.getdata("user").isNullOrBlank()){
            //SI EL USUARIO EXISTE EN SESION, OBTENGO INFO
            val user= gson.fromJson(sharedPref.getdata("user"), User::class.java)

            if (!sharedPref.getdata("rol").isNullOrBlank()) {
                //SI EL USURUARIO SELECCIONO UN ROL
                val rol = sharedPref.getdata("rol")?.replace("\"", "")
                if (rol == "COMERCIO"){
                    goToRestaurantHome()
                }
                else if (rol == "CLIENTE"){
                    goToClientHome()
                }
                else if (rol == "REPARTIDOR"){
                    goToDeliveryHome()
                }

            }
        else {
            goToClientHome()
        }

        }

    }
    private fun isValidForm(email: String, password: String ): Boolean {
        if (email.isBlank()) {
            return false
        }
        if (password.isBlank()) {
            return false
        }
        if (!email.isEmailValid()) {
            return false
        }
        return true
    }

    private fun goToRegister() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)

    }
}

