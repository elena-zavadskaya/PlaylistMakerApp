package com.practicum.playlistmakerapp.search.data.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.search.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences) : SearchHistoryRepository {

    companion object {
        private const val PREF_KEY_SEARCH_HISTORY = "search_history"
        private const val MAX_HISTORY_SIZE = 10
    }

    override fun addTrack(track: Track) {
        val tracksFromSearchHistory = getTracks().toMutableList()
        tracksFromSearchHistory.removeIf { it.trackId == track.trackId }
        if (tracksFromSearchHistory.size >= MAX_HISTORY_SIZE) {
            tracksFromSearchHistory.removeAt(tracksFromSearchHistory.size - 1)
        }
        tracksFromSearchHistory.add(0, track)
        sharedPreferences.edit()
            .putString(PREF_KEY_SEARCH_HISTORY, Gson().toJson(tracksFromSearchHistory))
            .apply()
    }

    override fun clearHistory() {
        sharedPreferences.edit()
            .remove(PREF_KEY_SEARCH_HISTORY)
            .apply()
    }

    override fun getTracks(): ArrayList<Track> {
        val json = sharedPreferences.getString(PREF_KEY_SEARCH_HISTORY, null) ?: return arrayListOf()
        return ArrayList(Gson().fromJson(json, Array<Track>::class.java).toList())
    }
}