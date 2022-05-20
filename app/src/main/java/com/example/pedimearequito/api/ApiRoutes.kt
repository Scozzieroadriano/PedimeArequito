package com.example.pedimearequito.api

import com.example.pedimearequito.routes.*

class ApiRoutes {
     val API_URL = "http://192.168.0.101:3000/api/"
     //   val API_URL = "https://arequito-shop.herokuapp.com/api/"
      val retrofit = RetrofitClient()

        fun getUsersRoutes(): UsersRoutes {
            return retrofit.getClient(API_URL).create(UsersRoutes::class.java)
        }

    fun getUsersRoutesWithToken( token: String): UsersRoutes {
        return retrofit.getClientWithToken(API_URL, token).create(UsersRoutes::class.java)
    }

    fun getCategoriesRoutes( token: String): CategoriesRoutes {
        return retrofit.getClientWithToken(API_URL, token).create(CategoriesRoutes::class.java)
    }
    fun getAddressRoutes(token : String): AddressRoutes{
        return retrofit.getClientWithToken(API_URL, token).create(AddressRoutes::class.java)
    }

    fun getOrdersRoutes(token : String): OrdersRoutes{
        return retrofit.getClientWithToken(API_URL, token).create(OrdersRoutes::class.java)
    }
    fun getProductsRoutes( token: String): ProductRoutes {
        return retrofit.getClientWithToken(API_URL, token).create(ProductRoutes::class.java)
    }
    fun getPaymentsRoutes( token: String): PaymentsRoutes {
        return retrofit.getClientWithToken(API_URL, token).create(PaymentsRoutes::class.java)
    }

}