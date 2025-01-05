package com.practicum.playlistmakerapp.search.domain.impl

import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.search.domain.api.SearchInteractor
import com.practicum.playlistmakerapp.search.domain.api.SearchRepository

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {
    override suspend fun searchTracks(query: String): List<Track> {
        return searchRepository.searchTracks(query)
    }
}
