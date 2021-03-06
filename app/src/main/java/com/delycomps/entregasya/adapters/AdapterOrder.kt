package com.delycomps.entregasya.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.delycomps.entregasya.R
import com.delycomps.entregasya.model.Order


class AdapterOrder(
    private var listOrder: List<Order>,
    private val refListener: ListAdapterListener
) : RecyclerView.Adapter<AdapterOrder.OrderViewHolder>() {
    private lateinit var mContext: Context

    override fun getItemCount(): Int {
        return listOrder.size
    }

    interface ListAdapterListener { // create an interface
        fun onClickAtDetailOrder(order: Order, position: Int)  // create callback function
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        mContext = v.context
        return OrderViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun changeStatus(newStatus: String, position: Int, countImagesDelivery: Int, countImagesPickup: Int){
        listOrder[position].status = newStatus
        listOrder[position].countImagesDelivery = countImagesDelivery
        listOrder[position].countImagesPickup = countImagesPickup
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order: Order = listOrder[position]

        holder.itemOrderCode.text = order.orderCode
        holder.itemStatus.text = order.status
        holder.itemProducts.text = order.products
        holder.itemPickupAddress.text = order.pickupAddress
        holder.itemPickupContactName.text = order.pickupContactName
        holder.itemPickupPhone.text = order.pickupPhone
        holder.itemDeliveryAddress.text = order.deliveryAddress
        holder.itemDeliveryContactName.text = order.deliveryContactName
        holder.itemDeliveryPhone.text = order.deliveryPhone
        holder.itemDeliveryUbigeo.text = order.deliveryTextUbigeo
        holder.itemPickupUbigeo.text = order.pickupTextUbigeo
    }

inner class OrderViewHolder internal constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
        internal var itemOrderCode: TextView = itemView.findViewById(R.id.item_order_code)
        internal var itemStatus: TextView = itemView.findViewById(R.id.item_status)
        internal var itemProducts: TextView = itemView.findViewById(R.id.item_products)
        internal var itemPickupAddress: TextView = itemView.findViewById(R.id.item_pickup_address)
        internal var itemPickupContactName: TextView = itemView.findViewById(R.id.item_pickup_contact_name)
        internal var itemPickupPhone: TextView = itemView.findViewById(R.id.item_pickup_phone)
        internal var itemDeliveryAddress: TextView = itemView.findViewById(R.id.item_delivery_address)
        internal var itemDeliveryContactName: TextView = itemView.findViewById(R.id.item_delivery_contact_name)
        internal var itemDeliveryPhone: TextView = itemView.findViewById(R.id.item_delivery_phone)

        internal var itemDeliveryUbigeo: TextView = itemView.findViewById(R.id.item_delivery_ubigeo)
        internal var itemPickupUbigeo: TextView = itemView.findViewById(R.id.item_pickup_ubigeo)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val order = listOrder[position]
                refListener.onClickAtDetailOrder(order, position)
            }
        }

    }
}

