package com.practicum.playlistmakerapp.search.usecases

import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.search.domain.api.SearchHistoryRepository

class AddTrackToHistoryUseCase(private val repository: SearchHistoryRepository) {
    fun execute(track: Track) {
        repository.addTrack(track)
    }
}