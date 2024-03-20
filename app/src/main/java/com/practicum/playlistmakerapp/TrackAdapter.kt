package com.practicum.playlistmakerapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.util.Stack

class TrackAdapter(
    private val tracks: List<Track>,
    val onClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onClick(tracks[position])
            val context = holder.itemView.context
            val intent = Intent(context, AudioPlayerActivity::class.java)
            val gson = Gson()
            val json = gson.toJson(tracks[position])
            context.startActivity(intent.putExtra("KEY", json))
        }
    }

    override fun getItemCount() = tracks.size
}
