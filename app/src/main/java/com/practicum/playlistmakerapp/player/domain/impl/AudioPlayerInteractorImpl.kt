package com.practicum.playlistmakerapp.player.domain.impl

import com.practicum.playlistmakerapp.player.domain.interactor.AudioPlayerInteractor
import com.practicum.playlistmakerapp.player.domain.repository.TrackRepository

class AudioPlayerInteractorImpl(
    private val trackRepository: TrackRepository
) : AudioPlayerInteractor {

    override fun prepareTrack(url: String, onPrepared: () -> Unit, onError: () -> Unit) {
        trackRepository.prepareTrack(url, onPrepared, onError)
    }

    override fun playTrack() {
        trackRepository.playTrack()
    }

    override fun pauseTrack() {
        trackRepository.pauseTrack()
    }

    override fun getTrackPosition(): Int {
        return trackRepository.getTrackPosition()
    }

    override fun release() {
        trackRepository.release()
    }
}
