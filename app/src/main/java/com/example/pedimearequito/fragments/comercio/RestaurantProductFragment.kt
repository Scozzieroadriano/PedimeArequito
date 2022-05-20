package com.example.pedimearequito.fragments.comercio

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.comercio.categories.CategoriesCommerceListActivity
import com.example.pedimearequito.activities.comercio.products.MyListProductActivity
import com.example.pedimearequito.models.*
import com.example.pedimearequito.providers.CategoriesProvider
import com.example.pedimearequito.providers.ProductsProvider
import com.example.pedimearequito.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class RestaurantProductFragment : Fragment() {

    val TAG = "ProductFragment"
    var myView: View? = null
    var editTextName: EditText? = null
    var editTextDescription: EditText? = null
    var editTextPrice: EditText? = null
    var editTextQuantity: EditText? = null
    var imageViewProduct1: ImageView? = null
    var imageViewProduct2: ImageView? = null
    var imageViewProduct3: ImageView? = null
    var buttonCreate: Button? = null
    var spinnerCategories: Spinner? = null
    var imageFile1: File? = null
    var imageFile2: File? = null
    var imageFile3: File? = null
    var idCategory = ""
    var btnListProduct: Button? = null


    var categoriesProvider: CategoriesProvider? = null

    var productsProvider: ProductsProvider? = null
    var user: User? = null
    var sharedPref: SharedPref? = null
    var categories = ArrayList<Category>()

    var intent: Intent? = null



    var idUser: Category? = null
    var idClientProduct = 0




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_restaurant_product, container, false)

        editTextName = myView?.findViewById(R.id.edittext_name)
        editTextDescription = myView?.findViewById(R.id.edittext_description)
        editTextPrice = myView?.findViewById(R.id.edittext_price)
        editTextQuantity = myView?.findViewById(R.id.edittext_quantity)
        imageViewProduct1 = myView?.findViewById(R.id.imageview_image1)
        imageViewProduct2 = myView?.findViewById(R.id.imageview_image2)
        imageViewProduct3 = myView?.findViewById(R.id.imageview_image3)
        buttonCreate = myView?.findViewById(R.id.btn_create_product)
        spinnerCategories = myView?.findViewById(R.id.spinner_categories)
        btnListProduct = myView?.findViewById(R.id.btn_list_product)


        buttonCreate?.setOnClickListener { createProduct() }
        btnListProduct?.setOnClickListener { gotoListProduct(user!!) }

        imageViewProduct1?.setOnClickListener { selectImage(101) }
        imageViewProduct2?.setOnClickListener { selectImage(102) }
        imageViewProduct3?.setOnClickListener { selectImage(103) }

        sharedPref = SharedPref(requireActivity())


        getUserFromSession()

        idClientProduct = user?.id!!.toInt()
        categoriesProvider = CategoriesProvider(user?.sessionToken!!)
        productsProvider= ProductsProvider(user?.sessionToken!!)




    getCategories()
        return myView
    }
    private fun gotoListProduct(user: User) {
        val i = Intent(context, MyListProductActivity::class.java)
        i.putExtra("idUser", user.id )
        context?.startActivity(i)
    }

    private fun getCategories() {

        categoriesProvider?.getAll(user?.id!!)?.enqueue(object : Callback<ArrayList<Category>> {
            override fun onResponse(
                call: Call<ArrayList<Category>>, response: Response<ArrayList<Category>>
            ) {
                if (response.body() != null) {
                    categories = response.body()!!

                    val arrayAdapter = ArrayAdapter<Category>(
                        requireActivity(),
                        android.R.layout.simple_dropdown_item_1line,
                        categories
                    )
                    spinnerCategories?.adapter = arrayAdapter
                    spinnerCategories?.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                l: Long
                            ) {
                                idCategory = categories[position].id!!
                                Log.d(TAG, "Id Category: $idCategory")
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                        }
                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun createProduct() {
        val name = editTextName?.text.toString()
        val description = editTextDescription?.text.toString()
        val priceText = editTextPrice?.text.toString()
        val quantity = editTextQuantity?.text.toString()
        val idClientUser = user?.id!!.toInt()
        val files = ArrayList<File> ()


        if (isValidForm(name, description, priceText, quantity)) {

            files.add(imageFile1!!)
            files.add(imageFile2!!)
            files.add(imageFile3!!)

            val product = Product(
                name = name,
                description = description,
                price = priceText.toDouble(),
                stock = quantity,
                idCategory = idCategory,
                IdUser = idClientUser

            )

            ProgressDialogFragment.showProgressBar(requireActivity())


            productsProvider?.create(files, product )?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    ProgressDialogFragment.hideProgressBar(requireActivity())


                    Log.d(TAG, "Response: $response")
                    Log.d(TAG, "Body: $response.body()")
                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()

                    if(response.body()?.isSuccess == true) {
                        resetForm()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    ProgressDialogFragment.hideProgressBar(requireActivity())
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error al registrar el producto", Toast.LENGTH_LONG).show()
                }

            })
        }
    }
    private  fun resetForm(){
        editTextName?.setText("")
        editTextDescription?.setText("")
        editTextPrice?.setText("")
        imageFile1 = null
        imageFile2 = null
        imageFile3 = null
        editTextQuantity?.setText("")
        imageViewProduct1?.setImageResource(R.drawable.ic_image)
        imageViewProduct2?.setImageResource(R.drawable.ic_image)
        imageViewProduct3?.setImageResource(R.drawable.ic_image)
    }

    private fun isValidForm(name: String, descripion: String, price: String, quantity: String): Boolean {

        if (name.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Ingresa el nombre del producto", Toast.LENGTH_LONG)
                .show()
            return false
        }
        if (descripion.isNullOrBlank()) {
            Toast.makeText(
                requireContext(),
                "Ingresa una descripción del producto",
                Toast.LENGTH_LONG
            ).show()

            return false
        }
        if (price.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Ingresa el precio del producto", Toast.LENGTH_LONG)
                .show()

            return false
        }
        if (quantity.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Ingresa la cantidad del producto", Toast.LENGTH_LONG)
                .show()

            return false
        }
        if (imageFile1 == null) {
            Toast.makeText(requireContext(), "Selecciona imagen 1", Toast.LENGTH_LONG).show()

            return false
        }
        if (imageFile2 == null) {
            Toast.makeText(requireContext(), "Selecciona imagen 2", Toast.LENGTH_LONG).show()

            return false
        }
        if (imageFile3 == null) {
            Toast.makeText(requireContext(), "Selecciona imagen 3", Toast.LENGTH_LONG).show()

            return false
        }
        if (idCategory.isNullOrBlank()) {
            Toast.makeText(
                requireContext(),
                "Selecciona la categoría correspondiente",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {
                val fileUri = data?.data
                imageFile1 =
                    File(fileUri?.path) //ARCHIVO QUE SE GUARDA COMO IMAGEN EN EL SERVIDOR O STORAGE
                imageViewProduct1?.setImageURI(fileUri)
            } else if (requestCode == 102) {
                val fileUri = data?.data
                imageFile2 =
                    File(fileUri?.path) //ARCHIVO QUE SE GUARDA COMO IMAGEN EN EL SERVIDOR O STORAGE
                imageViewProduct2?.setImageURI(fileUri)
            }
            if (requestCode == 103) {
                val fileUri = data?.data
                imageFile3 =
                    File(fileUri?.path) //ARCHIVO QUE SE GUARDA COMO IMAGEN EN EL SERVIDOR O STORAGE
                imageViewProduct3?.setImageURI(fileUri)
            }


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Operacion cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImage(requestCode: Int) {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start(requestCode)
    }

     fun getUserFromSession() {

        val gson = Gson()


        if (!sharedPref?.getdata("user").isNullOrBlank() ) {
            //SI EL USUARIO EXISTE EN SESION, OBTENGO INFO
            user = gson.fromJson(sharedPref?.getdata("user"), User::class.java)
        }
     }
}