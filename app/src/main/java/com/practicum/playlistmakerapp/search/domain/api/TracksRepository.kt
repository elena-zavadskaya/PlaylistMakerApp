package com.practicum.playlistmakerapp.search.domain.api

import com.practicum.playlistmakerapp.player.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
}