package com.delycomps.entregasya.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class Order(
    @SerializedName("id_order")
    @Expose
    var idOrder: Int,
    @SerializedName("order_code")
    @Expose
    var orderCode: String,
    @SerializedName("products")
    @Expose
    var products: String,
    @SerializedName("pickup_contact_name")
    @Expose
    var pickupContactName: String,
    @SerializedName("pickup_document_type")
    @Expose
    var pickupDocumentType: String,
    @SerializedName("pickup_document")
    @Expose
    var pickupDocument: String,
    @SerializedName("pickup_phone")
    @Expose
    var pickupPhone: String,
    @SerializedName("pickup_address")
    @Expose
    var pickupAddress: String,
    @SerializedName("pickup_reference")
    @Expose
    var pickupReference: String,
    @SerializedName("pickup_contact_name_2")
    @Expose
    var pickupContactName_2: String,
    @SerializedName("pickup_document_type_2")
    @Expose
    var pickupDocumentType_2: String,
    @SerializedName("pickup_document_2")
    @Expose
    var pickupDocument2: String,
    @SerializedName("pickup_phone_2")
    @Expose
    var pickupPhone_2: String,
    @SerializedName("pickup_time")
    @Expose
    var pickupTime: String,
    @SerializedName("delivery_contact_name")
    @Expose
    var deliveryContactName: String,
    @SerializedName("delivery_document_type")
    @Expose
    var deliveryDocumentType: String,
    @SerializedName("delivery_document")
    @Expose
    var deliveryDocument: String,
    @SerializedName("delivery_phone")
    @Expose
    var deliveryPhone: String,
    @SerializedName("delivery_address")
    @Expose
    var deliveryAddress: String,
    @SerializedName("delivery_reference")
    @Expose
    var deliveryReference: String,
    @SerializedName("delivery_contact_name_2")
    @Expose
    var deliveryContactName2: String,
    @SerializedName("delivery_document_type_2")
    @Expose
    var deliveryDocumentType2: String,
    @SerializedName("delivery_document_2")
    @Expose
    var deliveryDocument2: String,
    @SerializedName("delivery_phone_2")
    @Expose
    var deliveryPhone2: String,
    @SerializedName("delivery_time")
    @Expose
    var deliveryTime: String,
    @SerializedName("type")
    @Expose
    var type: String,
    @SerializedName("status")
    @Expose
    var status: String,
    @SerializedName("date_created")
    @Expose
    var dateCreated: String
)
