package com.practicum.playlistmakerapp.player.usecases

import com.practicum.playlistmakerapp.player.domain.repository.TrackRepository

class UpdateTrackPositionUseCase(private val trackRepository: TrackRepository) :
    UseCase<Unit, Int> {
    override fun execute(params: Unit): Int {
        return trackRepository.getTrackPosition()
    }
}