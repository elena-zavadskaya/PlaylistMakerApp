package com.practicum.playlistmakerapp.player.domain.usecases

import com.practicum.playlistmakerapp.player.domain.repository.TrackRepository

class PauseTrackUseCase(private val trackRepository: TrackRepository) {
    fun pause() {
        trackRepository.pauseTrack()
    }
}