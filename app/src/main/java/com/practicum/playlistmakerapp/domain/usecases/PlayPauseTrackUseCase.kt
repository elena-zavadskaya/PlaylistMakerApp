package com.practicum.playlistmakerapp.domain.usecases

import com.practicum.playlistmakerapp.domain.repository.TrackRepository

// Use case для воспроизведения и паузы трека.
class PlayPauseTrackUseCase(private val trackRepository: TrackRepository) {
    fun play() {
        trackRepository.playTrack()
    }

    fun pause() {
        trackRepository.pauseTrack()
    }
}