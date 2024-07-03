package com.practicum.playlistmakerapp.domain.usecases

import com.practicum.playlistmakerapp.domain.repository.TrackRepository

class UpdateTrackPositionUseCase(private val trackRepository: TrackRepository) {
    fun execute(): Int {
        return trackRepository.getTrackPosition()
    }
}