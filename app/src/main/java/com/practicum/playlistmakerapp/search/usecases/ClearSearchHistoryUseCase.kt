package com.practicum.playlistmakerapp.search.usecases

import com.practicum.playlistmakerapp.search.domain.api.SearchHistoryRepository

class ClearSearchHistoryUseCase(private val repository: SearchHistoryRepository) {
    fun execute() {
        repository.clearSearchHistory()
    }
}