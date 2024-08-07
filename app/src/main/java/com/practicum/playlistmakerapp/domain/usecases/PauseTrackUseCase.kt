package com.practicum.playlistmakerapp.domain.usecases

import com.practicum.playlistmakerapp.domain.repository.TrackRepository

class PauseTrackUseCase(private val trackRepository: TrackRepository) {
    fun pause() {
        trackRepository.pauseTrack()
    }
}