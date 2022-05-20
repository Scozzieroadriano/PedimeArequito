package com.example.pedimearequito.providers

import com.example.pedimearequito.api.MercadoPagoApiRoutes
import com.example.pedimearequito.models.MercadoPagoCardTokenBody
import com.example.pedimearequito.routes.MercadoPagoRoutes
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call

class MercadoPagoProviders {

    var mercadoPagoRoutes: MercadoPagoRoutes? = null

    init {
        val api = MercadoPagoApiRoutes()
        mercadoPagoRoutes = api.getMercadoPagoRoutes()
    }

    fun getInstallments(bin: String, amount: String): Call<JsonArray>? {
        return mercadoPagoRoutes?.getInstallments(bin, amount)
    }

    fun createCardToken(mercadoPagoCardTokenBody: MercadoPagoCardTokenBody): Call<JsonObject>? {
        return mercadoPagoRoutes?.createCardToken(mercadoPagoCardTokenBody)
    }

}