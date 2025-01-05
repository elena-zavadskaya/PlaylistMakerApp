package com.practicum.playlistmakerapp.player.data.impl

import android.media.MediaPlayer
import com.practicum.playlistmakerapp.player.domain.repository.AudioPlayerRepository

class AudioPlayerImpl(
    private var mediaPlayer: MediaPlayer
) : AudioPlayerRepository {

    override fun prepare(url: String, onPrepared: () -> Unit, onError: () -> Unit) {
        resetMediaPlayer()
        mediaPlayer?.apply {
            try {
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener { onPrepared() }
                setOnErrorListener { _, _, _ -> onError(); true }
            } catch (e: IllegalStateException) {
                onError()
            }
        }
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

    private fun resetMediaPlayer() {
        mediaPlayer.release()
        mediaPlayer = MediaPlayer()
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            onComplete()
        }
    }
}
