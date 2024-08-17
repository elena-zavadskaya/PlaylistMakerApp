package com.practicum.playlistmakerapp.search.domain.repository

import com.practicum.playlistmakerapp.player.domain.models.Track

interface SearchHistoryRepository {
    fun addTrack(track: Track)
    fun clearSearchHistory()
    fun getTracks(): ArrayList<Track>
}