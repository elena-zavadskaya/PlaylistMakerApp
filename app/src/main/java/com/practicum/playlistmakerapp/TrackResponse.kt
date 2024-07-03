package com.practicum.playlistmakerapp

import com.practicum.playlistmakerapp.domain.models.Track

data class TrackResponse(
    val resultCount: String,
    val results: List<Track>)