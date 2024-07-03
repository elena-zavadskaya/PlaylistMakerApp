package com.practicum.playlistmakerapp.domain.repository

import android.media.MediaPlayer

interface AudioPlayerRepository {
    fun prepare(url: String)
    fun play()
    fun pause()
    fun getCurrentPosition(): Int
    fun release()
    fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener)
    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener)
}