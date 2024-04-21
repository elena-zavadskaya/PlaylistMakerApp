package com.practicum.playlistmakerapp

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.util.Stack

class TrackAdapter(
    private val tracks: List<Track>,
    val onClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder> () {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY  = 1000L
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, TrackAdapter.CLICK_DEBOUNCE_DELAY )
        }
        return current
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onClick(tracks[position])
            if (clickDebounce()) {
                val context = holder.itemView.context
                val intent = Intent(context, AudioPlayerActivity::class.java)
                val gson = Gson()
                val json = gson.toJson(tracks[position])
                context.startActivity(intent.putExtra("KEY", json))
            }
        }
    }

    override fun getItemCount() = tracks.size
}
