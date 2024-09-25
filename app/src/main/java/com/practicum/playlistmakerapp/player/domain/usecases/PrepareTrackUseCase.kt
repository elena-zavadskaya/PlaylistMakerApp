package com.practicum.playlistmakerapp.player.domain.usecases

import com.practicum.playlistmakerapp.player.domain.repository.TrackRepository

class PrepareTrackUseCase(private val trackRepository: TrackRepository) :
    UseCase<PrepareTrackUseCase.Params, Unit> {
    data class Params(
        val url: String,
        val onPrepared: () -> Unit,
        val onError: () -> Unit
    )

    override fun execute(params: Params) {
        trackRepository.prepareTrack(params.url, params.onPrepared, params.onError)
    }
}