package com.delycomps.entregasya.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class Tracking(
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("date_created")
    @Expose
    var dateCreated: String,
    @SerializedName("status")
    @Expose
    var status: String,
    @SerializedName("first_name")
    @Expose
    var driverFirstName: String,
    @SerializedName("last_name")
    @Expose
    var driverLastName: String,
    @SerializedName("plate_number")
    @Expose
    var plateNumber: String,

)
