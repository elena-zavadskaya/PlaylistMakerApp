package com.practicum.playlistmakerapp.search.domain.impl

import com.practicum.playlistmakerapp.search.domain.api.SearchInteractor
import com.practicum.playlistmakerapp.search.domain.api.SearchRepository

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    override fun searchTracks(expression: String, consumer: SearchInteractor.TracksConsumer) {
        val t = Thread {
            consumer.consume(repository.searchTracks(expression))
        }
        t.start()
    }
}