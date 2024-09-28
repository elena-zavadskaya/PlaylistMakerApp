package com.practicum.playlistmakerapp.search.domain

import com.practicum.playlistmakerapp.player.domain.models.Track

interface SearchHistoryInteractor {
    fun addTrackToHistory(track: Track)
    fun clearSearchHistory()
    fun getSearchHistory(): List<Track>
}