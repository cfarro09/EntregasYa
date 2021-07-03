package com.delycomps.entregasya.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.delycomps.entregasya.R
import com.delycomps.entregasya.model.Tracking
import com.github.vipulasri.timelineview.TimelineView
import kotlinx.android.synthetic.main.item_linetime.view.*

object VectorDrawableUtils {

    private fun getDrawable(context: Context, drawableResId: Int): Drawable? {
        return VectorDrawableCompat.create(context.resources, drawableResId, context.theme)
    }

    fun getDrawable(context: Context, drawableResId: Int, colorFilter: Int): Drawable {
        val drawable = getDrawable(context, drawableResId)
        drawable!!.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN)
        return drawable
    }

    fun getBitmap(context: Context, drawableId: Int): Bitmap {
        val drawable = getDrawable(context, drawableId)

        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}


class ExampleTimeLineAdapter(private val mFeedList: List<Tracking>) : RecyclerView.Adapter<ExampleTimeLineAdapter.TimeLineViewHolder>() {

    private lateinit var mLayoutInflater: LayoutInflater

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {

        if(!::mLayoutInflater.isInitialized) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }

        return TimeLineViewHolder(mLayoutInflater.inflate(R.layout.item_linetime, parent, false), viewType)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        val timeLineModel = mFeedList[position]

        when (timeLineModel.status) {
            "TO DO" -> {
                setMarker(holder, R.drawable.ic_marker_inactive, R.color.colorGrey500)
            }
            "DOING" -> {
                setMarker(holder, R.drawable.ic_marker_active, R.color.colorGrey500)
            }
            else -> {
                setMarker(holder, R.drawable.ic_marker, R.color.colorGrey500)
            }
        }
        when (position) {
            0 -> holder.imageItem.setBackgroundResource(R.drawable.x1tracking)
            1 -> holder.imageItem.setBackgroundResource(R.drawable.x2tracking)
            2 -> holder.imageItem.setBackgroundResource(R.drawable.x3tracking)
            3 -> holder.imageItem.setBackgroundResource(R.drawable.x4tracking)
            4 -> holder.imageItem.setBackgroundResource(if (true) R.drawable.x5tracking else R.drawable.x5gristracking)
            5 -> holder.imageItem.setBackgroundResource(R.drawable.x6tracking)
        }

        if (timeLineModel.dateCreated.isNotEmpty()) {
            holder.date.visibility = View.VISIBLE
            holder.date.text = timeLineModel.dateCreated
        } else
            holder.date.visibility = View.GONE

        holder.message.text = timeLineModel.message
    }

    private fun setMarker(holder: TimeLineViewHolder, drawableResId: Int, colorFilter: Int) {
        holder.timeline.marker = VectorDrawableUtils.getDrawable(
            holder.itemView.context,
            drawableResId,
            ContextCompat.getColor(holder.itemView.context, colorFilter)
        )
    }

    override fun getItemCount() = mFeedList.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

        val date: AppCompatTextView = itemView.text_timeline_date
        val message: AppCompatTextView = itemView.text_timeline_title
        val imageItem: ImageView = itemView.image_item
        val timeline: TimelineView = itemView.timeline

        init {
            timeline.initLine(viewType)
        }
    }

}