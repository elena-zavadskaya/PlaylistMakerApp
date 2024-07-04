package com.practicum.playlistmakerapp.domain.repository

interface AudioPlayerRepository {
    fun prepare(url: String, onPrepared: () -> Unit, onError: () -> Unit)
    fun play()
    fun pause()
    fun getCurrentPosition(): Int
    fun release()
}