package com.practicum.playlistmakerapp.search.domain.api

import com.practicum.playlistmakerapp.player.domain.models.Track

interface  TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer{
        fun consume(foundTracks: List<Track>?)
    }
}