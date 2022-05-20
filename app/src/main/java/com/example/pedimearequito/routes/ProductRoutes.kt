package com.example.pedimearequito.routes

import com.example.pedimearequito.models.Category
import com.example.pedimearequito.models.Product
import com.example.pedimearequito.models.ResponseHttp
import com.example.pedimearequito.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProductRoutes {

    @GET ("products/findByCategory/{id_category}")
    fun findByCategory(
        @Path("id_category") idCategory: String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Product>>

    @GET ("products/findByIdUser/{id_user}")
    fun findByIdUser(
        @Path("id_user") idUser: String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Product>>

    @Multipart
    @POST ("products/create")
    fun create (
        @Part images: Array<MultipartBody.Part?>,
        @Part ("product") product: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @Multipart
    @PUT ("products/update")
    fun update(
        @Part image: MultipartBody.Part,
        @Part ("product") product: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>
    @Multipart
    @PUT ("products/updateIMG2")
    fun updateIMG2(
        @Part image: MultipartBody.Part,
        @Part ("product") product: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>
    @Multipart
    @PUT ("products/updateIMG3")
    fun updateIMG3(
        @Part image: MultipartBody.Part,
        @Part ("product") product: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>



    @PUT ("products/updateWithoutImage")
    fun updateWithoutImage (
        @Body product: Product,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @POST ("products/delete/{id}")
    fun deleteProduct(
        @Path("id") idProduct: String,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

}