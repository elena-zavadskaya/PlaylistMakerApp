package com.practicum.playlistmakerapp.search.domain.api

import com.practicum.playlistmakerapp.player.domain.models.Track

interface SearchRepository {
    suspend fun searchTracks(expression: String): List<Track>
}
