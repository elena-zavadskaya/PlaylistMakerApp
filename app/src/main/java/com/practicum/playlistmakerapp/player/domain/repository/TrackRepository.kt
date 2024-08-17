package com.practicum.playlistmakerapp.player.domain.repository

import com.practicum.playlistmakerapp.player.domain.models.Track

interface TrackRepository {
    fun getTrack(trackId: String): Track?
    fun prepareTrack(url: String, onPrepared: () -> Unit, onError: () -> Unit)
    fun playTrack()
    fun pauseTrack()
    fun getTrackPosition(): Int
}