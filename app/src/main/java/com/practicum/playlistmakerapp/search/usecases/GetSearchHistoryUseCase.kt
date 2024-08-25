package com.practicum.playlistmakerapp.search.usecases

import com.practicum.playlistmakerapp.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmakerapp.player.domain.models.Track

class GetSearchHistoryUseCase(private val repository: SearchHistoryRepository) {
    fun execute(): List<Track> {
        return repository.getTracks()
    }
}