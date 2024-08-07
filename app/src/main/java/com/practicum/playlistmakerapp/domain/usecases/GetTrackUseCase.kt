package com.practicum.playlistmakerapp.domain.usecases

import com.practicum.playlistmakerapp.domain.repository.TrackRepository
import com.practicum.playlistmakerapp.domain.models.Track

class GetTrackUseCase(private val trackRepository: TrackRepository) : UseCase<String, Track?> {
    override fun execute(params: String): Track? {
        return trackRepository.getTrack(params)
    }
}