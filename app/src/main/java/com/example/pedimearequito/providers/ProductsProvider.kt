package com.example.pedimearequito.providers

import com.example.pedimearequito.api.ApiRoutes
import com.example.pedimearequito.models.Category
import com.example.pedimearequito.models.Product
import com.example.pedimearequito.models.ResponseHttp
import com.example.pedimearequito.models.User
import com.example.pedimearequito.routes.CategoriesRoutes
import com.example.pedimearequito.routes.ProductRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class ProductsProvider (val token: String) {

    private var productRoutes: ProductRoutes? = null

    init {
        val api = ApiRoutes()
        productRoutes = api.getProductsRoutes(token)
    }

    fun findByCategory(idCategory: String): Call<ArrayList<Product>>? {
        return productRoutes?.findByCategory(idCategory, token)
    }
    fun findByIdUser(idUser: String): Call<ArrayList<Product>>? {
        return productRoutes?.findByIdUser(idUser, token)
    }

    fun create(files: List<File>, product: Product): Call<ResponseHttp>? {

        val images = arrayOfNulls<MultipartBody.Part>(files.size)

        for (i in 0 until files.size) {
            val reqFile = RequestBody.create(MediaType.parse("image/*"), files[i])
            images[i] = MultipartBody.Part.createFormData("image", files[i].name, reqFile)
        }

        val requestBody = RequestBody.create(MediaType.parse("text/plain"), product.toJson())
        return productRoutes?.create(images, requestBody, token)
    }

    fun update(file: File, product: Product): Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), product.toJson())
        return productRoutes?.update(image, requestBody,token)
    }

    fun updateIMG2(file: File, product: Product): Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), product.toJson())
        return productRoutes?.updateIMG2(image, requestBody,token)
    }

    fun updateIMG3(file: File, product: Product): Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), product.toJson())
        return productRoutes?.updateIMG3(image, requestBody,token)
    }

    fun updateWithoutImage(product: Product): Call <ResponseHttp>? {
        return productRoutes?.updateWithoutImage(product, token)
    }

    fun deleteProduct(idProduct: String): Call <ResponseHttp>? {
        return productRoutes?.deleteProduct(idProduct, token)
    }

}