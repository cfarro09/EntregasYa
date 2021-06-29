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
    var data: User?,
    var error: ResError?
)

data class ResponseOrders(
    var success: Boolean,
    var msg: String?,
    var result: List<Order>
)

data class ResError(
    var mensaje: String?,
    var code: Int?
)

data class ResponseLocation(
    var success: Boolean,
    var data: List<ResLocation>?,
    var error: ResError?
)

data class ResLocation(
    var value: String?,
    var ubigeo: String?
)

data class BodyGet(
    var method: String,
    var data: JSONObject
)