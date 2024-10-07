package com.practicum.playlistmakerapp.search.domain.api

import com.practicum.playlistmakerapp.player.domain.models.Track

interface SearchHistoryRepository {
    fun addTrack(track: Track)
    fun clearHistory()
    fun getTracks(): ArrayList<Track>
}