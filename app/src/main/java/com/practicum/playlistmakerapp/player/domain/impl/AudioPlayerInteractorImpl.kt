package com.practicum.playlistmakerapp.player.domain.impl

import com.practicum.playlistmakerapp.player.domain.interactor.AudioPlayerInteractor
import com.practicum.playlistmakerapp.player.domain.repository.TrackRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AudioPlayerInteractorImpl(
    private val trackRepository: TrackRepository
) : AudioPlayerInteractor {

    private var isTrackingProgress = true

    override fun observeTrackProgress(): Flow<Int> = flow {
        while (isTrackingProgress) {
            val position = trackRepository.getTrackPosition()
            emit(position)
            delay(300)
        }
    }

    override fun prepareTrack(url: String, onPrepared: () -> Unit, onError: () -> Unit) {
        trackRepository.prepareTrack(url, onPrepared, onError)
    }

    override fun playTrack() {
        isTrackingProgress = true
        trackRepository.playTrack()
    }

    override fun pauseTrack() {
        isTrackingProgress = false
        trackRepository.pauseTrack()
    }

    override fun getTrackPosition(): Int {
        return trackRepository.getTrackPosition()
    }

    override fun release() {
        isTrackingProgress = false
        trackRepository.release()
    }
}