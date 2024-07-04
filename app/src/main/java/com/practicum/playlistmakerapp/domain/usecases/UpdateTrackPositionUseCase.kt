package com.practicum.playlistmakerapp.domain.usecases

import com.practicum.playlistmakerapp.domain.repository.TrackRepository

class UpdateTrackPositionUseCase(private val trackRepository: TrackRepository) : UseCase<Unit, Int> {
    override fun execute(params: Unit): Int {
        return trackRepository.getTrackPosition()
    }
}