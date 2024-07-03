package com.practicum.playlistmakerapp.domain.usecases

import com.practicum.playlistmakerapp.domain.repository.TrackRepository
import com.practicum.playlistmakerapp.domain.models.Track

// Use case для получения трека.
class GetTrackUseCase(private val trackRepository: TrackRepository) {
    fun execute(trackId: String): Track? {
        return trackRepository.getTrack(trackId)
    }
}