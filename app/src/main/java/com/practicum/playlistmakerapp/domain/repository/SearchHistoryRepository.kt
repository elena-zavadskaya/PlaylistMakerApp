package com.practicum.playlistmakerapp.domain.repository

import com.practicum.playlistmakerapp.domain.models.Track

interface SearchHistoryRepository {
    fun addTrack(track: Track)
    fun clearSearchHistory()
    fun getTracks(): ArrayList<Track>
}