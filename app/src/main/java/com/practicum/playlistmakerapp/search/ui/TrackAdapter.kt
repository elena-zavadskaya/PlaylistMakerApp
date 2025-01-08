package com.practicum.playlistmakerapp.search.ui

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmakerapp.databinding.TrackViewBinding
import com.practicum.playlistmakerapp.player.domain.models.Track

class TrackAdapter(
    private var tracks: List<Track>,
    private val onClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return tracks[position].trackId.hashCode().toLong()
    }

    private fun clickDebounce() {
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = TrackViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            if (isClickAllowed) {
                clickDebounce()
                onClick(track)
            }
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun updateTracks(newTracks: List<Track>) {
        val diffCallback = TrackDiffCallback(tracks, newTracks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        tracks = newTracks
        diffResult.dispatchUpdatesTo(this)
    }

    class TrackDiffCallback(
        private val oldList: List<Track>,
        private val newList: List<Track>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].trackId == newList[newItemPosition].trackId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
