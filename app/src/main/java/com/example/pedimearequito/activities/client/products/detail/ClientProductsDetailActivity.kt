package com.example.pedimearequito.activities.client.products.detail

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.pedimearequito.R
import com.example.pedimearequito.models.Product
import com.example.pedimearequito.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ClientProductsDetailActivity : AppCompatActivity() {
    val TAG = "ProductsDetail"
    var product: Product? = null
    val gson  = Gson()

    var imageSlider: ImageSlider? = null
    var textViewName: TextView? = null
    var textViewDescription: TextView? = null
    var textViewPrice: TextView? = null
    var textViewCounter: TextView? = null
    var imageViewAdd: ImageView? = null
    var imageViewRemove: ImageView? = null
    var buttonadd: Button? = null

    var counter = 1
    var productPrice= 0.0

    var sharedPref: SharedPref? =null
    var selectedProducts = ArrayList<Product>()

    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_detail)

        product= gson.fromJson(intent.getStringExtra("product"),Product::class.java)

        sharedPref = SharedPref(this)

        imageSlider = findViewById(R.id.image_slider)
        textViewName = findViewById(R.id.txt_productName)
        textViewDescription = findViewById(R.id.textview_description)
        textViewPrice = findViewById(R.id.textView_price)
        textViewCounter = findViewById(R.id.textView_counter)
        imageViewAdd = findViewById(R.id.imageview_add)
        imageViewRemove = findViewById(R.id.imageview_remove)
        buttonadd = findViewById(R.id.btn_add_product)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(product?.image1,ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image3, ScaleTypes.CENTER_CROP))

        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        toolbar?.title = "Detalle del Producto"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageSlider?.setImageList(imageList)

        textViewName?.text= product?.name
        textViewDescription?.text= product?.description
        textViewPrice?.text= "${"$ "}${product?.price}"


        imageViewAdd?.setOnClickListener { addItem() }
        imageViewRemove?.setOnClickListener { removeItem() }

        buttonadd?.setOnClickListener { addToBag()
        finish()}

        getProductsFromSharedPref()
    }

    private fun addToBag() {
        val index = getIndexOf(product?.idProduct!!) //Obtenemos indice del producto si existe en sharedpref
        if(index == -1) { //no existe el producto en shared pref, o no existe orden de compra
            if (product?.quantity == null) {
                product?.quantity = 1
            }
            selectedProducts.add(product!!)
        }
        else   { //ENTONCES SI YA EXISTE EL PRODUCTO EN SHARED PREF, EDITAMOS LA CANTIDAD PARA NO CREAR 2 ORDENES DISTINTAS DEL MISMO PRODUCTO
            selectedProducts[index].quantity = counter
        }

        sharedPref?.save("order", selectedProducts)
        Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
    }
    private fun getProductsFromSharedPref() {
        if (!sharedPref?.getdata("order").isNullOrBlank()) { //VALIDAMOS SI EXITE UNA ORDEN
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            selectedProducts = gson.fromJson(sharedPref?.getdata("order"), type)

            val index =getIndexOf(product?.idProduct!!)

            if (index != -1) {
                product?.quantity = selectedProducts [index].quantity
                textViewCounter?.text = "${product?.quantity}"

                productPrice= product?.price!! * product?.quantity!!
                textViewPrice?.text = "${"$ "}${productPrice}"

                buttonadd?.setText("Editar Orden de Compra")
                buttonadd?.backgroundTintList = ColorStateList.valueOf(Color.RED)
            }


            for (p in selectedProducts){
                Log.d(TAG, "Shared pref: $p")
            }
        }
    }

    //COMPARAMOS SI UN PRODUCTO YA EXISTE EN SHAREDPREF Y PODER EDITAR LA CANTIDAD DEL PRODUCTO SELECCIONADO
    private fun getIndexOf (idProduct: String): Int{
        var pos= 0
        for (p in selectedProducts){
            if (p.idProduct == idProduct){
                return pos
            }
            pos ++
        }
        return -1
    }
    private fun addItem(){
        counter++
        productPrice = product?.price!! * counter
        product?.quantity = counter
        textViewCounter?.text = "${product?.quantity}"
        textViewPrice?.text= "${"$ "}${productPrice}"

    }
    private fun removeItem(){
        if (counter > 1) {
            counter--
            productPrice = product?.price!! * counter
            product?.quantity = counter
            textViewCounter?.text = "${product?.quantity}"
            textViewPrice?.text= "${"$ "}${productPrice}"
        }
    }
}