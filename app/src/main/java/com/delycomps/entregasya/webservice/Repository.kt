package com.delycomps.entregasya.webservice

import com.delycomps.entregasya.model.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Repository {
    fun login (username: String, password: String, onResult: (isSuccess: Boolean, result: User?, message: String?) -> Unit)  {
        val oo = JSONObject()
        oo.put("username", username)
        oo.put("password", password)

        val body: RequestBody = RequestBody.create(MediaType.parse("application/json"), oo.toString())

        try {
            Connection.instance.login(body).enqueue(object : Callback<ResponseLogin> {
                override fun onResponse(call: Call<ResponseLogin>?, response: Response<ResponseLogin>?) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            val user:User = response.body()!!.result!!
                            onResult(true, user, null)
                        } else {
                            val  msg: String = if (response.body()!!.msg != null) response.body()!!.msg!!  else "Hubo un error vuelva a intentarlo"
                            onResult(false, null, msg)
                        }
                    } else {
                        onResult(false, null, "Hubo un error vuelva a intentarlo")
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

    fun register (firstName: String, lastName: String, email: String, username: String, password: String, onResult: (isSuccess: Boolean, result: User?, message: String?) -> Unit)  {
        val oo = JSONObject()
        oo.put("firstname", firstName)
        oo.put("lastname", lastName)
        oo.put("email", email)
        oo.put("username", username)
        oo.put("password", password)

        val body: RequestBody = RequestBody.create(MediaType.parse("application/json"), oo.toString())

        try {
            Connection.instance.register(body).enqueue(object :
                Callback<ResponseLogin> {
                override fun onResponse(call: Call<ResponseLogin>?, response: Response<ResponseLogin>?) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            val user: User = response.body()!!.result!!
                            onResult(true, user, null)
                        } else {
                            val  msg: String = if (response.body()!!.msg != null) response.body()!!.msg!!  else "Hubo un error vuelva a intentarlo"
                            onResult(false, null, msg)
                        }
                    } else {
                        onResult(false, null, "Hubo un error vuelva a intentarlo")
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

    fun getOrder (token: String, onResult: (isSuccess: Boolean, result: List<Order>?, message: String?) -> Unit)  {

        val oo = JSONObject()
        oo.put("method", "DDDDDD")
        oo.put("data", JSONObject())

        val body: RequestBody = RequestBody.create(MediaType.parse("application/json"), oo.toString())

        try {
            Connection.instance.getOrders(body, token).enqueue(object :
                Callback<ResponseOrders> {
                override fun onResponse(call: Call<ResponseOrders>?, response: Response<ResponseOrders>?) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            onResult(true, response.body()!!.result, null)
                        } else {
                            val  msg: String = if (response.body()!!.msg != null) response.body()!!.msg!!  else "Hubo un error vuelva a intentarlo"
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

}