package com.practicum.playlistmakerapp.media.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmakerapp.R

class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val playlistImage: ImageView = view.findViewById(R.id.playlistImage)
    val playlistName: TextView = view.findViewById(R.id.playlistName)
    val trackCount: TextView = view.findViewById(R.id.track_count)
}
