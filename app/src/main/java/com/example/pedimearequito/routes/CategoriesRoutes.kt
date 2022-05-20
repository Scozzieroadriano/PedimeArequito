package com.example.pedimearequito.routes

import com.example.pedimearequito.models.Category
import com.example.pedimearequito.models.ResponseHttp
import com.example.pedimearequito.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface CategoriesRoutes {

    @GET ("categories/getByIdUser/{id}")
    fun getByIdUser(
        @Path("id") idUser: String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Category>>

    @GET ("categories/getAll/{id_user}")
    fun getAll(
        @Path("id_user") idUserClient: String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Category>>



    @Multipart
    @POST ("categories/create")
    fun create (
        @Part image: MultipartBody.Part,
        @Part ("category") category: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>



}