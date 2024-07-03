package com.practicum.playlistmakerapp.domain.usecases

import com.practicum.playlistmakerapp.domain.repository.TrackRepository

// Use case для подготовки трека к воспроизведению.
class PrepareTrackUseCase(private val trackRepository: TrackRepository) {
    fun execute(url: String, onPrepared: () -> Unit, onError: () -> Unit) {
        trackRepository.prepareTrack(url, onPrepared, onError)
    }
}