package com.delycomps.entregasya.webservice

import com.delycomps.entregasya.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Endpoints {

    @POST("auth/login")
    fun login(@Body body: RequestBody): Call<ResponseLogin>

    @POST("auth/register")
    fun register(@Body body: RequestBody): Call<ResponseLogin>

    @POST("main")
    fun getOrders(@Body body: RequestBody, @Header("Authorization") authHeader: String): Call<ResponseOrders>

    @POST("main/simpleTransaction")
    fun execute(@Body body: RequestBody, @Header("Authorization") authHeader: String): Call<ResponseCommon>

    @POST("main")
    fun executeSimple(@Body body: RequestBody, @Header("Authorization") authHeader: String): Call<ResponseCommon>

    @POST("main")
    fun getUbigeo(@Body body: RequestBody, @Header("Authorization") authHeader: String): Call<ResponseLocation>

    @POST("main")
    fun getMotives(@Body body: RequestBody, @Header("Authorization") authHeader: String): Call<ResponseMotives>

    @POST("main")
    fun getTracking(@Body body: RequestBody, @Header("Authorization") authHeader: String): Call<ResponseTracking>

    @POST("main")
    fun getImages(@Body body: RequestBody, @Header("Authorization") authHeader: String): Call<ResponseListImage>

    @Multipart
    @POST("pedido/imagen")
    fun uploadImage(@Part imagen: MultipartBody.Part, @Header("Authorization") authHeader: String): Call<ResponseImage>

    @Multipart
    @POST("pedido/imagenchofer")
    fun uploadImageDriver(@Part imagen: MultipartBody.Part, @Part("type") type: RequestBody, @Part("id_order") id_order: RequestBody, @Part("descripcion") descripcion: RequestBody, @Header("Authorization") authHeader: String): Call<ResponseImage>
}