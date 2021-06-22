package com.delycomps.entregasya.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("token")
    @Expose
    var token: String,
    @SerializedName("fullname")
    @Expose
    var fullName: String,
    @SerializedName("role")
    @Expose
    var role: String,



    @SerializedName("contactid")
    @Expose
    var contactId: Int,
    @SerializedName("firstname")
    @Expose
    var firstName: String,
    @SerializedName("lastname")
    @Expose
    var lastName: String,
    @SerializedName("document")
    @Expose
    var document: String,
    @SerializedName("city")
    @Expose
    var city: String,
    @SerializedName("address")
    @Expose
    var address: String,
    @SerializedName("email")
    @Expose
    var email: String,
    @SerializedName("phone")
    @Expose
    var phone: String
)
