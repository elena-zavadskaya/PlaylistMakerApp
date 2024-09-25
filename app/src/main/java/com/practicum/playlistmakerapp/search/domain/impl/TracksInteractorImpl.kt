package com.practicum.playlistmakerapp.search.domain.impl

import com.practicum.playlistmakerapp.search.domain.api.TracksInteractor
import com.practicum.playlistmakerapp.search.domain.api.TracksRepository

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        val t = Thread {
            consumer.consume(repository.searchTracks(expression))
        }
        t.start()
    }
}