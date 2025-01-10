package com.practicum.playlistmakerapp.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.media.domain.model.Playlist

class BottomSheetAdapter(
    private val playlists: List<Playlist>,
    private val onPlaylistClicked: (Playlist) -> Unit
) : RecyclerView.Adapter<BottomSheetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlists, parent, false)
        return BottomSheetViewHolder(view, onPlaylistClicked)
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size
}