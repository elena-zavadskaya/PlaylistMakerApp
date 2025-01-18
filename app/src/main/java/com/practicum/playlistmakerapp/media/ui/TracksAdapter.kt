package com.practicum.playlistmakerapp.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.databinding.TrackViewBinding
import com.practicum.playlistmakerapp.player.domain.models.Track

class TracksAdapter(
    private var tracks: List<Track>,
    private val onTrackClick: (Track) -> Unit,
    private val onTrackLongClick: (Track) -> Unit
) : ListAdapter<Track, TracksAdapter.TrackViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = TrackViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }

    fun updateTracks(newTracks: List<Track>) {
        this.tracks = newTracks
        submitList(newTracks)
    }

    inner class TrackViewHolder(private val binding: TrackViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(track: Track) {
            binding.trackName.text = track.trackName
            binding.artistName.text = track.artistName
            binding.trackTime.text = track.trackTimeMillis

            Glide.with(binding.root.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.album)
                .into(binding.trackImage)

            binding.root.setOnClickListener {
                onTrackClick(track)
            }

            binding.root.setOnLongClickListener {
                onTrackLongClick(track)
                true
            }
        }
    }

    class TrackDiffCallback : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.trackId == newItem.trackId
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem == newItem
        }
    }
}

