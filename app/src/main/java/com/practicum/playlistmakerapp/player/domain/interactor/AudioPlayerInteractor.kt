package com.practicum.playlistmakerapp.player.domain.interactor

import kotlinx.coroutines.flow.Flow

interface AudioPlayerInteractor {
    fun observeTrackProgress(): Flow<Int>
    fun prepareTrack(url: String, onPrepared: () -> Unit, onError: () -> Unit)
    fun playTrack()
    fun pauseTrack()
    fun getTrackPosition(): Int
    fun release()
}
