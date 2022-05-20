package com.example.pedimearequito.routes

import com.example.pedimearequito.models.MercadoPagoCardTokenBody
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MercadoPagoRoutes {

    @GET("v1/payment_methods/installments?access_token=TEST-5796990703182923-011817-2c1fa80b0f1454a17791fba7ff28db6d-160536287")
    fun getInstallments(@Query("bin") bin: String, @Query("amount") amount: String): Call<JsonArray>

    @POST("v1/card_tokens?public_key=TEST-678dc956-9f7d-4a33-abe0-50658c4bce55")
    fun createCardToken(@Body body: MercadoPagoCardTokenBody): Call<JsonObject>
}