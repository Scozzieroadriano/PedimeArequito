package com.example.pedimearequito.providers

import com.example.pedimearequito.api.ApiRoutes
import com.example.pedimearequito.models.MercadoPagoPayment
import com.example.pedimearequito.models.ResponseHttp
import com.example.pedimearequito.routes.PaymentsRoutes
import retrofit2.Call



class PaymentsProvider(val token: String) {

    private var paymentsRoutes: PaymentsRoutes? = null

    init {
        val api = ApiRoutes()
        paymentsRoutes = api.getPaymentsRoutes(token)
    }

    fun create(mercadoPagoPayment: MercadoPagoPayment): Call<ResponseHttp>? {
        return paymentsRoutes?.createPayment(mercadoPagoPayment, token)
    }

}