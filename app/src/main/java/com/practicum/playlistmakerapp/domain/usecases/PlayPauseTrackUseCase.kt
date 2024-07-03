package com.practicum.playlistmakerapp.domain.usecases

import com.practicum.playlistmakerapp.domain.repository.TrackRepository

class PlayPauseTrackUseCase(private val trackRepository: TrackRepository) {
    fun play() {
        trackRepository.playTrack()
    }

    fun pause() {
        trackRepository.pauseTrack()
    }
}