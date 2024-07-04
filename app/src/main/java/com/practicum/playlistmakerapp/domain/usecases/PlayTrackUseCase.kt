package com.practicum.playlistmakerapp.domain.usecases

import com.practicum.playlistmakerapp.domain.repository.TrackRepository

class PlayTrackUseCase(private val trackRepository: TrackRepository) {
    fun play() {
        trackRepository.playTrack()
    }
}