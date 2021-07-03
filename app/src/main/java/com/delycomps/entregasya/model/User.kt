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
    @SerializedName("type")
    @Expose
    var type: String,
    @SerializedName("contactid")
    @Expose
    var contactId: Int,
    @SerializedName("first_name")
    @Expose
    var firstName: String,
    @SerializedName("last_name")
    @Expose
    var lastName: String,
    @SerializedName("doc_number")
    @Expose
    var document: String,
    @SerializedName("doc_type")
    @Expose
    var docType: String,
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
