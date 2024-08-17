package com.practicum.playlistmakerapp.player.usecases

import com.practicum.playlistmakerapp.player.domain.repository.TrackRepository

class PauseTrackUseCase(private val trackRepository: TrackRepository) {
    fun pause() {
        trackRepository.pauseTrack()
    }
}