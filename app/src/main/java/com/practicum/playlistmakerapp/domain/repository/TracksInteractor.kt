package com.practicum.playlistmakerapp.domain.repository

import com.practicum.playlistmakerapp.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundMovies: List<Track>)
    }
}