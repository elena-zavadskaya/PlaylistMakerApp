package com.practicum.playlistmakerapp.search.data.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.search.domain.api.SearchHistoryRepository

const val SEARCH_HISTORY_KEY = "key_for_dark_theme"
const val MAX_SIZE = 10

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SearchHistoryRepository {

    override fun addTrack(track: Track) {
        val tracksFromSearchHistory = getTracks().toMutableList()
        tracksFromSearchHistory.removeIf { it.trackId == track.trackId }
        if (tracksFromSearchHistory.size >= MAX_SIZE) {
            tracksFromSearchHistory.removeAt(tracksFromSearchHistory.size - 1)
        }
        tracksFromSearchHistory.add(0, track)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, gson.toJson(tracksFromSearchHistory))
            .apply()
    }

    override fun clearSearchHistory() {
        sharedPreferences.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

    override fun getTracks(): ArrayList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return arrayListOf()
        return ArrayList(Gson().fromJson(json, Array<Track>::class.java).toList())
    }
}