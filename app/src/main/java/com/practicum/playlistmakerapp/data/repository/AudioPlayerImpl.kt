package com.practicum.playlistmakerapp.data.repository

import android.media.MediaPlayer

class AudioPlayerImpl {

    private val mediaPlayer = MediaPlayer()

    fun prepare(url: String, onPrepared: () -> Unit, onError: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { onPrepared() }
        mediaPlayer.setOnErrorListener { _, _, _ -> onError(); true }
    }

    fun play() {
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun release() {
        mediaPlayer.release()
    }
}
