package com.example.pedimearequito.fragments.comercio

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pedimearequito.R
import com.example.pedimearequito.models.Category
import com.example.pedimearequito.models.ResponseHttp
import com.example.pedimearequito.models.User
import com.example.pedimearequito.providers.CategoriesProvider
import com.example.pedimearequito.providers.UsersProvider
import com.example.pedimearequito.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class ComercioCreateCategoriFragment : Fragment() {
    val TAG = "CategoryFragment"
    var myView: View?= null
    var imageViewCategory: ImageView? = null
    var editTextCategory: EditText? = null
    var buttonCreate: Button? = null

    private var imageFile: File? = null

    var categoriesProvider: CategoriesProvider? = null
    var usersProvider: UsersProvider? = null
    var sharedPref: SharedPref? = null
    var user: User? = null
    var idCategory = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_comercio_create_categori, container, false)

        sharedPref= SharedPref(requireActivity())

        imageViewCategory = myView?.findViewById(R.id.imageview_category)
        editTextCategory = myView?.findViewById(R.id.edittext_category)
        buttonCreate = myView?.findViewById(R.id.btn_createcat)




        imageViewCategory?.setOnClickListener { selectImage() }
        buttonCreate?.setOnClickListener { createCategory() }

        getUserFromSession()
        usersProvider = UsersProvider(user?.sessionToken!!)
        categoriesProvider = CategoriesProvider(user?.sessionToken!!)

        idCategory = user?.id!!.toInt()

        return myView
    }

    private fun createCategory(){

        val name = editTextCategory?.text.toString()


        if (imageFile != null) {
            val category = Category(
                name = name,
                idUser = idCategory
            )


            categoriesProvider?.create(imageFile!!, category)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                 Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()

                if (response.body()?.isSuccess == true) {
                    clearForm()
                }
            }
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                 })
        }
        else {
            Toast.makeText(requireContext(), "Selecciona una imagen", Toast.LENGTH_LONG).show()
        }
    }
    private fun clearForm(){
        editTextCategory?.setText("")
        imageFile= null
        imageViewCategory?.setImageResource(R.drawable.ic_image)
    }

    private val startImageForResult=
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = File(fileUri?.path) //ARCHIVO QUE SE GUARDA COMO IMAGEN EN EL SERVIDOR O STORAGE
                imageViewCategory?.setImageURI(fileUri)
            }
            else if ( resultCode == ImagePicker.RESULT_ERROR){
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(requireContext(),"La operación ha sido cancelada", Toast.LENGTH_LONG).show()
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
    private  fun getUserFromSession(){

        val gson= Gson()

        if (!sharedPref?.getdata("user").isNullOrBlank()){
            //SI EL USUARIO EXISTE EN SESION, OBTENGO INFO
            user= gson.fromJson(sharedPref?.getdata("user"), User::class.java)

        }

    }


}