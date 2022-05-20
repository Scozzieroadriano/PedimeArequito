package com.example.pedimearequito.activities.comercio.products.detailandupdate


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.comercio.home.RestaurantHomeActivity
import com.example.pedimearequito.models.Product
import com.example.pedimearequito.models.ResponseHttp
import com.example.pedimearequito.providers.ProductsProvider
import com.example.pedimearequito.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetailAndUpdateProduct : AppCompatActivity() {

    val TAG = "ProductsUpdate"
    var product: Product? = null
    val gson = Gson()

    var editTextNameProduct: EditText? = null
    var editTextDescription: EditText? = null
    var editTextPrice: EditText? = null
    var editTextQuantity: EditText? = null


    var imageViewP1: ImageView? = null
    var imageViewP2: ImageView? = null
    var imageViewP3: ImageView? = null

    var buttonRemove: Button? = null
    var buttonUpdate: Button? = null

    var imageFile1: File? = null
    var imageFile2: File? = null
    var imageFile3: File? = null

    var sharedPref: SharedPref? = null
    var toolbar: Toolbar? = null

    var productProvider: ProductsProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_and_update_product)


        sharedPref = SharedPref(this)

        product = gson.fromJson(intent.getStringExtra("product"), Product::class.java)

        productProvider = ProductsProvider(product!!.idProduct!!)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        toolbar?.title = "Volver"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editTextNameProduct = findViewById(R.id.edittext_name)
        editTextDescription = findViewById(R.id.edittext_description)
        editTextPrice = findViewById(R.id.edittext_price)
        editTextQuantity = findViewById(R.id.edittext_quantity)


        imageViewP1 = findViewById(R.id.imageview_image1)
        imageViewP2 = findViewById(R.id.imageview_image2)
        imageViewP3 = findViewById(R.id.imageview_image3)

        buttonRemove = findViewById(R.id.button_deleteproduct)
        buttonUpdate = findViewById(R.id.btn_update_product)

        editTextNameProduct?.setText(product?.name)
        editTextDescription?.setText(product?.description)
        editTextPrice?.setText("${product?.price}")
        editTextQuantity?.setText(product?.stock)

        if (!product?.image1.isNullOrBlank()) {
            Glide.with(this).load(product?.image1).into(imageViewP1!!)
        }
        if (!product?.image2.isNullOrBlank()) {
            Glide.with(this).load(product?.image2).into(imageViewP2!!)
        }
        if (!product?.image3.isNullOrBlank()) {
            Glide.with(this).load(product?.image3).into(imageViewP3!!)
        }
        imageViewP1?.setOnClickListener { selectImage(101) }
        imageViewP2?.setOnClickListener { selectImage(102) }
        imageViewP3?.setOnClickListener { selectImage(103) }





        buttonUpdate?.setOnClickListener { updateProduct(); goToProductFragment() }


        buttonRemove?.setOnClickListener{deleteProduct()}
    }


    private fun updateProduct() {
        val name = editTextNameProduct?.text.toString()
        val description = editTextDescription?.text.toString()
        val price = editTextPrice?.text.toString()
        val stock = editTextQuantity?.text.toString()

        product?.name = name
        product?.description = description
        product?.price = price.toDouble()
        product?.stock = stock

        ProgressDialogFragment.showProgressBar(this)
        if (imageFile1 != null ) {

            productProvider?.update(imageFile1!!, product!!)
                ?.enqueue(object : Callback<ResponseHttp> {
                    override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>
                    ) {
                        Log.d(TAG, "RESPONSE: $response")
                        Log.d(TAG, "BODY: ${response.body()}")

                        Toast.makeText(
                            this@DetailAndUpdateProduct, response.body()?.message, Toast.LENGTH_SHORT).show()

                    }


                    override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                        Log.d(TAG, "Error: ${t.message}")
                        Toast.makeText(
                            this@DetailAndUpdateProduct, "Error: ${t.message}", Toast.LENGTH_SHORT).show()

                    }
                })


        } else {

            productProvider?.updateWithoutImage(product!!)
                ?.enqueue(object : Callback<ResponseHttp> {
                    override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>
                    ) {
                        Log.d(TAG, "ERROR: $response")
                        Log.d(TAG, "ERROR: ${response.body()}")
                        Toast.makeText(
                            this@DetailAndUpdateProduct, "Datos del producto Actualizados", Toast.LENGTH_SHORT).show()

                    }

                    override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                        Log.d(TAG, "ERROR: ${t.message}")
                        Toast.makeText(
                            this@DetailAndUpdateProduct, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        if (imageFile2 != null) {

            productProvider?.updateIMG2(imageFile2!!, product!!)
                ?.enqueue(object : Callback<ResponseHttp> {
                    override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>
                    ) {
                        Log.d(TAG, "RESPONSE: $response")
                        Log.d(TAG, "BODY: ${response.body()}")

                        Toast.makeText(
                            this@DetailAndUpdateProduct, response.body()?.message, Toast.LENGTH_SHORT).show()


                    }

                    override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                        Log.d(TAG, "Error: ${t.message}")
                        Toast.makeText(
                            this@DetailAndUpdateProduct, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

        }

        if (imageFile3 != null) {

            productProvider?.updateIMG3(imageFile3!!, product!!)
                ?.enqueue(object : Callback<ResponseHttp> {
                    override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>
                    ) {
                        Log.d(TAG, "RESPONSE: $response")
                        Log.d(TAG, "BODY: ${response.body()}")

                        Toast.makeText(
                            this@DetailAndUpdateProduct, response.body()?.message, Toast.LENGTH_SHORT).show()

                        ProgressDialogFragment.hideProgressBar(this@DetailAndUpdateProduct)
                    }

                    override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                        ProgressDialogFragment.hideProgressBar(this@DetailAndUpdateProduct)
                        Log.d(TAG, "Error: ${t.message}")
                        Toast.makeText(
                            this@DetailAndUpdateProduct, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        ProgressDialogFragment.hideProgressBar(this@DetailAndUpdateProduct)



    }

    private fun goToProductFragment (){
        val i = Intent(this , RestaurantHomeActivity::class.java)
                startActivity(i)

    }
    private fun deleteProduct () {
        productProvider?.deleteProduct(product?.idProduct!!)?.enqueue(object : Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                Log.d(TAG, "ERROR: $response")
                Log.d(TAG, "ERROR: ${response.body()}")
                Toast.makeText(this@DetailAndUpdateProduct,"Producto Eliminado",Toast.LENGTH_LONG).show()
                goToProductFragment()
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Log.d(TAG, "ERROR: ${t.message}")
                Toast.makeText(this@DetailAndUpdateProduct,"Error: ${t.message}",Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                val fileUri = data?.data
                imageFile1 =
                    File(fileUri?.path) //ARCHIVO QUE SE GUARDA COMO IMAGEN EN EL SERVIDOR O STORAGE
                imageViewP1?.setImageURI(fileUri)
            } else if (requestCode == 102) {
                val fileUri = data?.data
                imageFile2 =
                    File(fileUri?.path) //ARCHIVO QUE SE GUARDA COMO IMAGEN EN EL SERVIDOR O STORAGE
                imageViewP2?.setImageURI(fileUri)
            }
            if (requestCode == 103) {
                val fileUri = data?.data
                imageFile3 =
                    File(fileUri?.path) //ARCHIVO QUE SE GUARDA COMO IMAGEN EN EL SERVIDOR O STORAGE
                imageViewP3?.setImageURI(fileUri)
            }


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Operacion Cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImage(requestCode: Int) {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start(requestCode)
    }
    private fun saveProductInSession(data: String) {

        val gson = Gson()
        val product = gson.fromJson(data, Product::class.java)
        sharedPref?.save("product", product)


    }



        }