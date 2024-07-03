package com.practicum.playlistmakerapp

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmakerapp.domain.models.Track

const val SEARCH_HISTORY_KEY = "key_for_dark_theme"
const val MAX_SIZE = 10

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun addTrack(track: Track) {
        val tracksFromSearchHistory = getTracks()
        tracksFromSearchHistory.removeIf { it.trackId == track.trackId }
        if (tracksFromSearchHistory.size >= MAX_SIZE) {
            tracksFromSearchHistory.removeAt(tracksFromSearchHistory.size - 1)
        }
        tracksFromSearchHistory.add(0, track)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, Gson().toJson(tracksFromSearchHistory))
            .apply()
    }

    fun clearSearchHistory() {
        sharedPreferences.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

    fun getTracks() : ArrayList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return arrayListOf()
        return ArrayList(Gson().fromJson(json, Array<Track>::class.java).toList())
    }
}