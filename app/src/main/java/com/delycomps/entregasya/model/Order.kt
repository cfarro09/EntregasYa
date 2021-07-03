package com.delycomps.entregasya.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class Order : Parcelable {
    @SerializedName("id_order")
    @Expose
    var idOrder: Int

    @SerializedName("guide_number")
    @Expose
    var orderCode: String?

    @SerializedName("products")
    @Expose
    var products: String?

    @SerializedName("pickup_contact_name")
    @Expose
    var pickupContactName: String?

    @SerializedName("pickup_phone")
    @Expose
    var pickupPhone: String?

    @SerializedName("pickup_address")
    @Expose
    var pickupAddress: String?

    @SerializedName("delivery_contact_name")
    @Expose
    var deliveryContactName: String?

    @SerializedName("delivery_phone")
    @Expose
    var deliveryPhone: String?

    @SerializedName("delivery_address")
    @Expose
    var deliveryAddress: String?
    @Expose
    var type: String?
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("date_created")
    @Expose
    var dateCreated: String? = null
    @SerializedName("date_date_updated")
    @Expose
    var dateUpdated: String?
    @SerializedName("images_product")
    @Expose
    var imagesProduct: String?
    @SerializedName("images_order")
    @Expose
    var imagesOrder: String?

    @SerializedName("pickup_ubigeo")
    @Expose
    var pickupTextUbigeo: String?
    @SerializedName("delivery_ubigeo")
    @Expose
    var deliveryTextUbigeo: String?

    @SerializedName("pickup_reference")
    @Expose
    var pickupReference: String?
    @SerializedName("delivery_reference")
    @Expose
    var deliveryReference: String?

    constructor(
        idOrder: Int,
        orderCode: String,
        products: String,
        pickupContactName: String,
        pickupPhone: String,
        pickupAddress: String,
        deliveryContactName: String,
        deliveryPhone: String,
        deliveryAddress: String,
        type: String,
        status: String,
        dateCreated: String,
        dateUpdated: String,
        imagesProduct: String,
        imagesOrder: String,
        pickupTextUbigeo: String,
        deliveryTextUbigeo: String,
        pickupReference: String,
        deliveryReference: String
    ) {
        this.idOrder = idOrder
        this.orderCode = orderCode
        this.products = products
        this.pickupContactName = pickupContactName
        this.pickupPhone = pickupPhone
        this.pickupAddress = pickupAddress
        this.deliveryContactName = deliveryContactName
        this.deliveryPhone = deliveryPhone
        this.deliveryAddress = deliveryAddress
        this.type = type
        this.status = status
        this.dateCreated = dateCreated
        this.dateUpdated = dateUpdated
        this.imagesProduct = imagesProduct
        this.imagesOrder = imagesOrder
        this.pickupTextUbigeo = pickupTextUbigeo
        this.deliveryTextUbigeo = deliveryTextUbigeo
        this.pickupReference = pickupReference
        this.deliveryReference = deliveryReference
    }

    protected constructor(parcel: Parcel) {
        idOrder = parcel.readInt()
        orderCode = parcel.readString()
        products = parcel.readString()
        pickupContactName = parcel.readString()
        pickupPhone = parcel.readString()
        pickupAddress = parcel.readString()
        deliveryContactName = parcel.readString()
        deliveryPhone = parcel.readString()
        deliveryAddress = parcel.readString()
        type = parcel.readString()
        status = parcel.readString()
        dateCreated = parcel.readString()
        dateUpdated = parcel.readString()
        imagesProduct = parcel.readString()
        imagesOrder = parcel.readString()
        pickupTextUbigeo = parcel.readString()
        deliveryTextUbigeo = parcel.readString()
        pickupReference = parcel.readString()
        deliveryReference = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeInt(idOrder)
        p0.writeString(orderCode)
        p0.writeString(products)
        p0.writeString(pickupContactName)
        p0.writeString(pickupPhone)
        p0.writeString(pickupAddress)
        p0.writeString(deliveryContactName)
        p0.writeString(deliveryPhone)
        p0.writeString(deliveryAddress)
        p0.writeString(type)
        p0.writeString(status)
        p0.writeString(dateCreated)
        p0.writeString(dateUpdated)
        p0.writeString(imagesProduct)
        p0.writeString(imagesOrder)
        p0.writeString(pickupTextUbigeo)
        p0.writeString(deliveryTextUbigeo)
        p0.writeString(pickupReference)
        p0.writeString(deliveryReference)
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }


}
