package com.delycomps.entregasya.webservice

import android.util.Log
import com.delycomps.entregasya.model.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class Repository {


    fun login(
        username: String,
        password: String,
        onResult: (isSuccess: Boolean, result: User?, message: String?) -> Unit
    )  {
        val oo = JSONObject()
        oo.put("usr", username)
        oo.put("password", password)
        oo.put("origin", "APP")

        val data = JSONObject()
        data.put("data", oo)

        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            data.toString()
        )

        try {
            Connection.instance.login(body).enqueue(object : Callback<ResponseLogin> {
                override fun onResponse(
                    call: Call<ResponseLogin>?,
                    response: Response<ResponseLogin>?
                ) {
                    if (response!!.isSuccessful) {
                        val user: User = response.body()!!.data!!
                        onResult(true, user, null)
                    } else {
                        val message = JSONObject(response.errorBody().string()).getJSONObject("error").getString("mensaje")
                        onResult(false, null, message)
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>?, t: Throwable?) {
                    onResult(false, null, "Hubo un error vuelva a intentarlo")
                }
            })
        } catch (e: java.lang.Exception){
            onResult(false, null, "Hubo un error vuelva a intentarlo")
        }
    }

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String,
        onResult: (isSuccess: Boolean, result: User?, message: String?) -> Unit
    )  {
        val data = JSONObject()
        data.put("id_user", 0)
        data.put("username", username)
        data.put("first_name", firstName)
        data.put("last_name", lastName)
        data.put("phone", "")
        data.put("doc_type", "DNI")
        data.put("doc_number", "")
        data.put("user_email", email)
        data.put("password", password)
        data.put("status", "ACTIVO")
        data.put("type", "CLIENT")

        val oo = JSONObject()
        oo.put("method", "SP_INS_USER")
        oo.put("data", data)

        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            oo.toString()
        )

        try {
            Connection.instance.register(body).enqueue(object :
                Callback<ResponseLogin> {
                override fun onResponse(
                    call: Call<ResponseLogin>?,
                    response: Response<ResponseLogin>?
                ) {
                    if (response!!.isSuccessful) {
                        val user: User = response.body()!!.data!!
                        onResult(true, user, null)
                    } else {
                        val message = JSONObject(response.errorBody().string()).getJSONObject("error").getString("mensaje")
                        onResult(false, null, message)
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>?, t: Throwable?) {
                    onResult(false, null, "Hubo un error vuelva a intentarlo")
                }
            })
        } catch (e: java.lang.Exception){
            onResult(false, null, "Hubo un error vuelva a intentarlo")
        }
    }

    fun getOrder(
        type: String,
        token: String,
        all: Boolean = false,
        onResult: (isSuccess: Boolean, result: List<Order>?, message: String?) -> Unit
    )  {

        val oo = JSONObject()

        if (all) {
            oo.put("method", "SP_SEL_ORDER_STATUS")
            oo.put("data", JSONObject())
        } else {
            val data = JSONObject()
            data.put("status", JSONObject.NULL)
            data.put("type", type)

            oo.put("method", "SP_SEL_CLIENT_ORDER")
            oo.put("data", data)
        }

        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            oo.toString()
        )

        try {
            Connection.instance.getOrders(body, token).enqueue(object :
                Callback<ResponseOrders> {
                override fun onResponse(
                    call: Call<ResponseOrders>?,
                    response: Response<ResponseOrders>?
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            onResult(true, response.body()!!.data, null)
                        } else {
                            val msg: String =
                                if (response.body()!!.msg != null) response.body()!!.msg!! else "Hubo un error vuelva a intentarlo"
                            onResult(false, null, msg)
                        }
                    } else {
                        onResult(false, null, "Hubo un error vuelva a intentarlo")
                    }
                }

                override fun onFailure(call: Call<ResponseOrders>?, t: Throwable?) {
                    onResult(false, null, "Hubo un error vuelva a intentarlo")
                }
            })
        } catch (e: java.lang.Exception){
            onResult(false, null, "Hubo un error vuelva a intentarlo")
        }
    }


    fun getUbigeo(
        search: String,
        filter: String,
        token: String,
        onResult: (isSuccess: Boolean, result: List<ResLocation>?, message: String?) -> Unit
    )  {
        val data = JSONObject()
        data.put("search", search)
        data.put("filter", filter)

        val oo = JSONObject()
        oo.put("method", "SP_UBIGEO")
        oo.put("data", data)

        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            oo.toString()
        )

        try {
            Connection.instance.getUbigeo(body, token).enqueue(object :
                Callback<ResponseLocation> {
                override fun onResponse(
                    call: Call<ResponseLocation>?,
                    response: Response<ResponseLocation>?
                ) {
                    if (response!!.isSuccessful) {
                        onResult(true, response.body()!!.data, null)
                    } else {
                        val message = JSONObject(response.errorBody().string()).getJSONObject("error").getString("mensaje")
                        onResult(false, null, message)
                    }
                }
                override fun onFailure(call: Call<ResponseLocation>?, t: Throwable?) {
                    onResult(false, null, "Hubo un error vuelva a intentarlo")
                }
            })
        } catch (e: java.lang.Exception){
            onResult(false, null, "Hubo un error vuelva a intentarlo")
        }
    }

    fun getTracking(
        idOrder: Int,
        token: String,
        onResult: (isSuccess: Boolean, result: List<Tracking>?, message: String?) -> Unit
    )  {
        val data = JSONObject()
        data.put("id_order", idOrder)

        val oo = JSONObject()
        oo.put("method", "SP_SEL_ORDER_TRACKING")
        oo.put("data", data)

        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            oo.toString()
        )

        try {
            Connection.instance.getTracking(body, token).enqueue(object :
                Callback<ResponseTracking> {
                override fun onResponse(
                    call: Call<ResponseTracking>?,
                    response: Response<ResponseTracking>?
                ) {
                    if (response!!.isSuccessful) {
                        onResult(true, response.body()!!.data, null)
                    } else {
                        val message = JSONObject(response.errorBody().string()).getJSONObject("error").getString("mensaje")
                        onResult(false, null, message)
                    }
                }
                override fun onFailure(call: Call<ResponseTracking>?, t: Throwable?) {
                    onResult(false, null, "Hubo un error vuelva a intentarlo")
                }
            })
        } catch (e: java.lang.Exception){
            onResult(false, null, "Hubo un error vuelva a intentarlo")
        }
    }


    fun executeMain(
        jo: JSONObject,
        transaction: Boolean,
        token: String,
        onResult: (isSuccess: Boolean, result: String?, message: String?) -> Unit
    )  {
        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json"),
            jo.toString()
        )
        Log.d("jo_new_order", jo.toString())

        try {
            if (transaction) {
                Connection.instance.execute(body, token).enqueue(object :
                    Callback<ResponseCommon> {
                    override fun onResponse(
                        call: Call<ResponseCommon>?,
                        response: Response<ResponseCommon>?
                    ) {
                        if (response!!.isSuccessful) {
                            onResult(true, "Se guardó satisfactoriamente", null)
                        } else {
                            val message = JSONObject(response.errorBody().string()).getJSONObject("error").getString("mensaje")
                            onResult(false, null, message)
                        }
                    }
                    override fun onFailure(call: Call<ResponseCommon>?, t: Throwable?) {
                        onResult(false, null, "Hubo un error vuelva a intentarlo")
                    }
                })
            } else {
                Connection.instance.executeSimple(body, token).enqueue(object :
                    Callback<ResponseCommon> {
                    override fun onResponse(
                        call: Call<ResponseCommon>?,
                        response: Response<ResponseCommon>?
                    ) {
                        if (response!!.isSuccessful) {
                            onResult(true, "Se guardó satisfactoriamente", null)
                        } else {
                            val message = JSONObject(response.errorBody().string()).getJSONObject("error").getString("mensaje")
                            onResult(false, null, message)
                        }
                    }
                    override fun onFailure(call: Call<ResponseCommon>?, t: Throwable?) {
                        onResult(false, null, "Hubo un error vuelva a intentarlo")
                    }
                })
            }
        } catch (e: java.lang.Exception){
            onResult(false, null, "Hubo un error vuelva a intentarlo")
        }
    }

    fun uploadImage(
        file: File,
        token: String,
        onResult: (isSuccess: Boolean, result: String?, message: String?) -> Unit
    )  {
        val fileReqBody: RequestBody = RequestBody.create(MediaType.parse("*/*"), file)
        val part: MultipartBody.Part = MultipartBody.Part.createFormData("imagen", file.name, fileReqBody)

        try {
            Connection.instance.uploadImage(part, token).enqueue(object :
                Callback<ResponseImage> {
                override fun onResponse(
                    call: Call<ResponseImage>?,
                    response: Response<ResponseImage>?
                ) {
                    if (response!!.isSuccessful) {
                        onResult(true, response.body()!!.data!!.url, null)
                    } else {
                        val message = JSONObject(response.errorBody().string()).getJSONObject("error").getString("mensaje")
                        onResult(false, null, message)
                    }
                }
                override fun onFailure(call: Call<ResponseImage>?, t: Throwable?) {
                    onResult(false, null, "Hubo un error vuelva a intentarlo")
                }
            })
        } catch (e: java.lang.Exception){
            onResult(false, null, "Hubo un error vuelva a intentarlo")
        }
    }


}