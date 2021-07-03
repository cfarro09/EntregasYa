package com.delycomps.entregasya.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.delycomps.entregasya.R

class AdapterGallery(private var list: List<String>) : RecyclerView.Adapter<AdapterGallery.OrderViewHolder>() {
    private lateinit var mContext: Context
    private val listImage: MutableList<String> = list.toMutableList()

    override fun getItemCount(): Int {
        return listImage.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_galeria, parent, false)
        mContext = v.context
        return OrderViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun addItem(newImage: String){
        listImage.add(newImage)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order: String = listImage[position]

        Glide.with(mContext)
            .load(order)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageGallery)
    }

inner class OrderViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        internal var imageGallery: ImageView = itemView.findViewById(R.id.item_image_gallery)
    }
}

