package com.delycomps.entregasya.model

import org.json.JSONObject
import java.util.*

data class Response(
    var success: Boolean,
    var msg: String?,
    var result: Any = false
)

data class ResponseLogin(
    var success: Boolean,
    var msg: String?,
    var result: User?
)

data class ResponseOrders(
    var success: Boolean,
    var msg: String?,
    var result: List<Order>
)

data class BodyGet(
    var method: String,
    var data: JSONObject
)