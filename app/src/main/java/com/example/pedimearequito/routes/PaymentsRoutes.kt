package com.example.pedimearequito.routes

import com.example.pedimearequito.models.Category
import com.example.pedimearequito.models.MercadoPagoPayment
import com.example.pedimearequito.models.ResponseHttp
import com.example.pedimearequito.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface PaymentsRoutes {


    @POST ("payments/create")
    fun createPayment (
        @Body mercadoPagoPayment: MercadoPagoPayment,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>



}