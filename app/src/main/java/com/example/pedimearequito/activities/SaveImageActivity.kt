package com.example.pedimearequito.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.client.home.ClientHomeActivity
import com.example.pedimearequito.models.ResponseHttp
import com.example.pedimearequito.models.User
import com.example.pedimearequito.providers.UsersProvider
import com.example.pedimearequito.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivity : AppCompatActivity() {
    val TAG = "SaveImageActivity"

    var circleImageUser: CircleImageView? = null
    var buttonNext: Button?= null
    var buttonConfirm: Button? =null

    private var imageFile: File?= null

    var usersProvider : UsersProvider?= null
    var user: User? =null
    var sharedPref: SharedPref? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)

        sharedPref= SharedPref(this)
        getUserFromSession()

        usersProvider= UsersProvider(user?.sessionToken)

        circleImageUser= findViewById(R.id.circleimage_user)
        buttonNext= findViewById(R.id.btn_saltarpaso)
        buttonConfirm= findViewById(R.id.btn_confirmar)

        circleImageUser?.setOnClickListener { selectImage() }
        buttonNext?.setOnClickListener { goToClientHome() }
        buttonConfirm?.setOnClickListener { saveImage() }
    }

    private fun saveImage (){

        if (imageFile !=null && user !=null){

            usersProvider?.update(imageFile!!, user!!)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                    if (response.body()?.isSuccess == true){
                        saveUserInSession(response.body()?.data.toString())
                        Toast.makeText(this@SaveImageActivity, "Foto de perfil actualizada ", Toast.LENGTH_LONG).show()

                    }
                }
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@SaveImageActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
        else {
            Toast.makeText(this , "La imagen y los datos de sesión no deben ser nulos", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveUserInSession(data: String) {

        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref?.save("user", user)
        goToClientHome()

    }
    private val startImageForResult=
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result: ActivityResult ->

            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = File(fileUri?.path) //ARCHIVO QUE SE GUARDA COMO IMAGEN EN EL SERVIDOR O STORAGE
                circleImageUser?.setImageURI(fileUri)
            }
            else if ( resultCode == ImagePicker.RESULT_ERROR){
                Toast.makeText(this,ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this,"La operación ha sido cancelada", Toast.LENGTH_LONG).show()
            }
    }
    private fun selectImage(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)


            }
    }
    private fun goToClientHome(){
        val i= Intent(this, ClientHomeActivity::class.java)
        i.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //eliminar historial de pantalla
        startActivity(i)


    }

    private  fun getUserFromSession(){


        val gson= Gson()

        if (!sharedPref?.getdata("user").isNullOrBlank()){
            //SI EL USUARIO EXISTE EN SESION, OBTENGO INFO
             user= gson.fromJson(sharedPref?.getdata("user"), User::class.java)

        }

    }
}