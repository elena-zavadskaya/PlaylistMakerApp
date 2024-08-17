package com.practicum.playlistmakerapp.player.data.network

import com.practicum.playlistmakerapp.player.domain.models.Track

data class TrackResponse(
    val resultCount: String,
    val results: List<Track>)