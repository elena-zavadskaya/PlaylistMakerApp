package com.practicum.playlistmakerapp.search.domain.impl

import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.search.domain.interactor.SearchHistoryInteractor
import com.practicum.playlistmakerapp.search.domain.api.SearchHistoryRepository

class SearchHistoryInteractorImpl(
    private val searchHistoryRepository: SearchHistoryRepository
) : SearchHistoryInteractor {

    override fun addTrackToHistory(track: Track) {
        searchHistoryRepository.addTrack(track)
    }

    override fun clearSearchHistory() {
        searchHistoryRepository.clearHistory()
    }

    override fun getSearchHistory(): List<Track> {
        return searchHistoryRepository.getTracks()
    }
}
