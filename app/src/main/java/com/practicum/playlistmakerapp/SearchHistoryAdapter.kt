package com.practicum.playlistmakerapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchHistoryAdapter(val onClick: (Track) -> Unit) : RecyclerView.Adapter<TrackViewHolder> () {
    var historyTracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = historyTracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(historyTracks[position])
        holder.itemView.setOnClickListener { onClick(historyTracks[position]) }
    }
}