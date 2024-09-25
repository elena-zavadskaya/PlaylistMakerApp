package com.practicum.playlistmakerapp.player.domain.usecases

import com.practicum.playlistmakerapp.player.domain.repository.TrackRepository

class PlayTrackUseCase(private val trackRepository: TrackRepository) {
    fun play() {
        trackRepository.playTrack()
    }
}