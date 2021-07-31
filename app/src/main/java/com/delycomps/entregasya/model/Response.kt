package com.delycomps.entregasya.model

import org.json.JSONObject
import java.util.*

data class ResponseImage(
    var success: Boolean,
    var msg: String?,
    var data: DataImage?
)

data class ResponseListImage(
    var success: Boolean,
    var msg: String?,
    var data: List<DataImage>
)

data class ResponseCommon(
    var success: Boolean,
    var msg: String?
)

data class ResponseLogin(
    var success: Boolean,
    var msg: String?,
    var data: User?,
    var error: ResError?
)

data class DataImage(
    var url: String,
    var type: String
)

data class ResponseOrders(
    var success: Boolean,
    var msg: String?,
    var data: List<Order>
)

data class ResponseTracking(
    var success: Boolean,
    var msg: String?,
    var data: List<Tracking>
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