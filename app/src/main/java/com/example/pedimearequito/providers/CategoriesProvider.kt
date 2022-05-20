package com.example.pedimearequito.providers

import com.example.pedimearequito.api.ApiRoutes
import com.example.pedimearequito.models.Category
import com.example.pedimearequito.models.ResponseHttp
import com.example.pedimearequito.routes.CategoriesRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class CategoriesProvider ( val token: String) {

    private var categoriesRoutes: CategoriesRoutes? = null

    init {
        val api = ApiRoutes()
        categoriesRoutes = api.getCategoriesRoutes(token)
    }

    fun getByIdUser(idUser: String): Call<ArrayList<Category>>? {
        return categoriesRoutes?.getByIdUser(idUser, token)
    }

    fun getAll(idUserCLient: String): Call<ArrayList<Category>>? {
        return categoriesRoutes?.getAll(idUserCLient, token)
    }
    fun create(file: File, category: Category): Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), category.toJson())


        return categoriesRoutes?.create(image, requestBody, token)

    }
}