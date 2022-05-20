package com.example.pedimearequito.providers

import com.example.pedimearequito.api.ApiRoutes
import com.example.pedimearequito.models.Address
import com.example.pedimearequito.models.Category
import com.example.pedimearequito.models.ResponseHttp
import com.example.pedimearequito.models.User
import com.example.pedimearequito.routes.AddressRoutes
import com.example.pedimearequito.routes.CategoriesRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class AddressProvider(val token: String) {

    private var addressRoutes: AddressRoutes? = null

    init {
        val api = ApiRoutes()
        addressRoutes = api.getAddressRoutes(token)
    }

    fun getAddress(idUser: String): Call<ArrayList<Address>>? {
        return addressRoutes?.getAddress(idUser, token)
    }

    fun create(address: Address): Call<ResponseHttp>? {
        return addressRoutes?.create(address, token)
    }

}