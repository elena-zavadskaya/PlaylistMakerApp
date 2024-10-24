package com.practicum.playlistmakerapp.player.domain.interactor

interface AudioPlayerInteractor {
    fun prepareTrack(url: String, onPrepared: () -> Unit, onError: () -> Unit)
    fun playTrack()
    fun pauseTrack()
    fun getTrackPosition(): Int
    fun release()
}
