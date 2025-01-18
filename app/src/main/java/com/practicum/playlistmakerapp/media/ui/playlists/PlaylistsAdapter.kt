package com.practicum.playlistmakerapp.media.ui.playlists

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import com.practicum.playlistmakerapp.databinding.PlaylistItemBinding

class PlaylistsAdapter(
    var playlists: List<PlaylistEntity>,
    private val onPlaylistClick: (Int) -> Unit
) : RecyclerView.Adapter<PlaylistsAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(private val binding: PlaylistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: PlaylistEntity) {
            binding.root.setOnClickListener {
                onPlaylistClick(playlist.id.toInt())
            }

            binding.playlistName.text = playlist.name

            binding.trackCount.text = "${playlist.trackCount} ${getTrackWord(playlist.trackCount)}"

            if (playlist.coverImagePath.isNotEmpty()) {
                val imageUri = Uri.parse(playlist.coverImagePath)
                binding.playlistImage.setImageURI(imageUri)
            } else {
                binding.playlistImage.setImageResource(R.drawable.placeholder)
            }
        }

        private fun getTrackWord(count: Int): String {
            return when {
                count % 10 == 1 && count % 100 != 11 -> "трек"
                count % 10 in 2..4 && (count % 100 !in 12..14) -> "трека"
                else -> "треков"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size
}
