package com.practicum.playlistmakerapp.presentation.ui.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.domain.models.Track
import com.practicum.playlistmakerapp.presentation.ui.player.AudioPlayerActivity

class SearchHistoryAdapter(
    val onClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder> () {
    var historyTracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = historyTracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(historyTracks[position])
//        holder.itemView.setOnClickListener { onClick(historyTracks[position]) }
        holder.itemView.setOnClickListener {
            onClick(historyTracks[position])
            val context = holder.itemView.context
            val intent = Intent(context, AudioPlayerActivity::class.java)
            val gson = Gson()
            val json = gson.toJson(historyTracks[position])
            context.startActivity(intent.putExtra("KEY", json))
        }
    }
}