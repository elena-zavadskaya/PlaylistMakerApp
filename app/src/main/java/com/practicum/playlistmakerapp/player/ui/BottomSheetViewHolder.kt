package com.practicum.playlistmakerapp.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.media.domain.model.Playlist

class BottomSheetViewHolder(
    itemView: View,
    private val onPlaylistClicked: (Playlist) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val playlistImage: ImageView = itemView.findViewById(R.id.playlistImage)
    private val playlistName: TextView = itemView.findViewById(R.id.playlist_name)
    private val trackCount: TextView = itemView.findViewById(R.id.track_count)

    fun bind(playlist: Playlist) {
        playlistName.text = playlist.name
        trackCount.text = "${playlist.trackCount} треков"
        Glide.with(itemView.context)
            .load(playlist.coverImagePath)
            .placeholder(R.drawable.placeholder)
            .into(playlistImage)

        itemView.setOnClickListener { onPlaylistClicked(playlist) }
    }
}