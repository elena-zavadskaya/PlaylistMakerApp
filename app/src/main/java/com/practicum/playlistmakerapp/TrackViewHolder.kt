package com.practicum.playlistmakerapp

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    private fun dpToPx(dp: Float, view: ImageView): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            view.resources.displayMetrics).toInt()
    }

    fun bind(model: Track) {
        trackName.text = model.trackName

        Glide.with(trackImage)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, trackImage)))
            .into(trackImage)

        artistName.text = model.artistName
        trackTime.text = model.trackTime
    }
}
