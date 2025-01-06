package com.practicum.playlistmakerapp.search.domain.api

import com.practicum.playlistmakerapp.player.domain.models.Track

interface  SearchInteractor {
    suspend fun searchTracks(query: String): List<Track>
}