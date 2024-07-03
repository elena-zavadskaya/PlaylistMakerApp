package com.practicum.playlistmakerapp.domain.repository

import com.practicum.playlistmakerapp.domain.models.Track

interface TrackRepository {
    fun getTrack(trackId: String): Track?
    fun prepareTrack(url: String, onPrepared: () -> Unit, onError: () -> Unit)
    fun playTrack()
    fun pauseTrack()
    fun getTrackPosition(): Int
}