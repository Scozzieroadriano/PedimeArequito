package com.example.pedimearequito.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pedimearequito.R
import com.example.pedimearequito.models.ResponseHttp
import com.example.pedimearequito.models.User
import com.example.pedimearequito.providers.UsersProvider
import com.example.pedimearequito.utils.SharedPref
import com.google.gson.Gson

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {

    val TAG = "RegisterActivity"

    var imageViewRegister: ImageView? = null
    var editTextName: EditText? = null
    var editTextapellido: EditText? = null
    var editTextEmail: EditText? = null
    var editTextPhone: EditText? = null
    var editTextPassword: EditText? = null
    var editTextConfirmPassword: EditText? = null
    var buttonRegister: Button? = null

    var usersProvider = UsersProvider()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        imageViewRegister = findViewById(R.id.imgregister)

        editTextName = findViewById(R.id.txt_Nombre)
        editTextapellido = findViewById(R.id.txt_Apellido)
        editTextEmail = findViewById(R.id.txt_email)
        editTextPhone = findViewById(R.id.txt_telefono)
        editTextPassword = findViewById(R.id.txt_Password)
        editTextConfirmPassword = findViewById(R.id.txt_confirmarPassword)
        buttonRegister = findViewById(R.id.btn_registro)

        imageViewRegister?.setOnClickListener {goToLogin()}
        buttonRegister?.setOnClickListener { register() }
    }
    private fun register (){
        val name = editTextName?.text.toString()
        val lastname = editTextapellido?.text.toString()
        val email = editTextEmail?.text.toString()
        val phone = editTextPhone?.text.toString()
        val password = editTextPassword?.text.toString()
        val cpassword = editTextConfirmPassword?.text.toString()

        if (isValidForm(name = name, lastname = lastname, email = email, phone = phone, password = password, cpassword = cpassword)) {

            val user = User(
                name = name,
                lastname = lastname,
                email = email,
                phone = phone,
                password = password
            )
            usersProvider.register(user)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {

                    if (response.body()?.isSuccess == true) {
                        saveUserInSession(response.body()?.data.toString())
                        goToClientHome()
                    }

                    Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "Response: ${response.body()}")
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {

                    Log.d(TAG, "Se produjo un error ${t.message}")
                    Toast.makeText(this@RegisterActivity, "Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun goToClientHome(){
        val i = Intent(this, SaveImageActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // eliminar historial de pantalla

        startActivity(i)
    }

    private fun saveUserInSession (data: String) {
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data,User::class.java)
        sharedPref.save("user", user)
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
    private fun isValidForm (
        name: String,
        lastname: String,
        email: String,
        phone: String,
        password: String,
        cpassword: String,

        ): Boolean{

        if (name.isBlank()){
            Toast.makeText(this, "Debes ingresar el nombre", Toast.LENGTH_SHORT).show()
            return false
        }

        if (lastname.isBlank()){
            Toast.makeText(this, "Debes ingresar el apellido", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email.isBlank()){
            Toast.makeText(this, "Debes ingresar el email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (phone.isBlank()){
            Toast.makeText(this, "Debes ingresar el phone", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isBlank()){
            Toast.makeText(this, "Debes ingresar el password", Toast.LENGTH_SHORT).show()
            return false
        }
        if (cpassword.isBlank()){
            Toast.makeText(this, "Debes repetir password", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!email.isEmailValid()){
            Toast.makeText(this, "Debes escribir el mail correctamente", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != cpassword) {
            Toast.makeText(this, "Los password deben ser iguales", Toast.LENGTH_SHORT).show()

            return false
        }

        return true

    }

    private fun goToLogin(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

}