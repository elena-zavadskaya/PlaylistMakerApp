package com.practicum.playlistmakerapp.search.data.dto

import com.practicum.playlistmakerapp.player.domain.models.Track

data class TrackResponse(
    val resultCount: String,
    val results: List<Track>)