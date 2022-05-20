package com.example.pedimearequito.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Category(

    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String? = null,
    @SerializedName ("id_user") val idUser: Int,
    @SerializedName ("user") val client: User? = null,


) {

    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "$name"    }


}



