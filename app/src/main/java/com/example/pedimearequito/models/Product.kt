package com.example.pedimearequito.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Product(
    @SerializedName("id") val idProduct: String? = null,
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String,
    @SerializedName("image1")val image1: String? = null,
    @SerializedName("image2")val image2: String? = null,
    @SerializedName("image3")val image3: String? = null,
    @SerializedName("id_category") val idCategory: String,
    @SerializedName("price") var price: Double,
    @SerializedName("quantity")var quantity: Int? = null,
    @SerializedName("stock")var stock: String? = null,
    @SerializedName("id_user")var IdUser: Int? = null
) {

    fun toJson(): String{
        return  Gson().toJson(this)
    }

    override fun toString(): String {
        return "Product(idProduct=$idProduct, name='$name', description='$description', image1=$image1, image2=$image2, image3=$image3, idCategory='$idCategory', price=$price, quantity=$quantity, stock=$stock, IdUser=$IdUser)"
    }


}