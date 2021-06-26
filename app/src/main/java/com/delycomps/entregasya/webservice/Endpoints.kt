package com.delycomps.entregasya.webservice

import com.delycomps.entregasya.model.Order
import com.delycomps.entregasya.model.ResponseLogin
import com.delycomps.entregasya.model.ResponseOrders
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Endpoints {

    @POST("auth/login")
    fun login(@Body body: RequestBody): Call<ResponseLogin>

    @POST("auth/register")
    fun register(@Body body: RequestBody): Call<ResponseLogin>

    @POST("main")
    fun getOrders(@Body body: RequestBody, @Header("Authorization") authHeader: String): Call<ResponseOrders>

}