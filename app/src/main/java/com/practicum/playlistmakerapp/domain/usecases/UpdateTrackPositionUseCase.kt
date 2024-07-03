package com.practicum.playlistmakerapp.domain.usecases

import com.practicum.playlistmakerapp.domain.repository.TrackRepository

// Use case для обновления позиции трека.
class UpdateTrackPositionUseCase(private val trackRepository: TrackRepository) {
    fun execute(): Int {
        return trackRepository.getTrackPosition()
    }
}