package com.practicum.playlistmakerapp.player.usecases

import com.practicum.playlistmakerapp.player.domain.repository.TrackRepository
import com.practicum.playlistmakerapp.player.domain.models.Track

class GetTrackUseCase(private val trackRepository: TrackRepository) : UseCase<String, Track?> {
    override fun execute(params: String): Track? {
        return trackRepository.getTrack(params)
    }
}