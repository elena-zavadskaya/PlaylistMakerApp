package com.practicum.playlistmakerapp.media.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity

class PlaylistsAdapter(
    var playlists: List<PlaylistEntity>
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.playlistName.text = playlist.name
        holder.trackCount.text = "${playlist.trackCount} треков"

        if (playlist.coverImagePath.isNotEmpty()) {
            val imageUri = Uri.parse(playlist.coverImagePath)
            holder.playlistImage.setImageURI(imageUri)
        } else {
            holder.playlistImage.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int = playlists.size
}


