package com.practicum.playlistmakerapp.data

interface AudioPlayerInterface {
    fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun play()
    fun pause()
    fun getCurrentPosition(): Int
    fun release()
}