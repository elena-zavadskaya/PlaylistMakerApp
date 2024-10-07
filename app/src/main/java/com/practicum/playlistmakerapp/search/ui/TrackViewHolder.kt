package com.practicum.playlistmakerapp.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.DpToPx
import com.practicum.playlistmakerapp.databinding.TrackViewBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(private val binding: TrackViewBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())

        Glide.with(binding.trackImage)
            .load(track.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(DpToPx.dpToPx(2f, itemView.findViewById(R.id.trackImage))))
            .into(binding.trackImage)
    }
}
