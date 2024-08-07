package com.practicum.playlistmakerapp.data.impl

import android.media.MediaPlayer
import com.practicum.playlistmakerapp.domain.repository.AudioPlayerRepository

class AudioPlayerImpl : AudioPlayerRepository {

    private val mediaPlayer = MediaPlayer()

    override fun prepare(url: String, onPrepared: () -> Unit, onError: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { onPrepared() }
        mediaPlayer.setOnErrorListener { _, _, _ -> onError(); true }
    }

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun release() {
        mediaPlayer.release()
    }
}
