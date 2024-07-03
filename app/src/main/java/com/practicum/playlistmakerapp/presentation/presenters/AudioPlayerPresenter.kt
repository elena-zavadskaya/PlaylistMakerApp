package com.practicum.playlistmakerapp.presentation.presenters

import com.practicum.playlistmakerapp.domain.models.Track
import com.practicum.playlistmakerapp.domain.usecases.GetTrackUseCase
import com.practicum.playlistmakerapp.domain.usecases.PlayPauseTrackUseCase
import com.practicum.playlistmakerapp.domain.usecases.PrepareTrackUseCase
import com.practicum.playlistmakerapp.domain.usecases.UpdateTrackPositionUseCase

// Презентер для экрана аудиоплеера. Он связывает бизнес-логику и представление.
class AudioPlayerPresenter(
    private val getTrackUseCase: GetTrackUseCase,
    private val prepareTrackUseCase: PrepareTrackUseCase,
    private val playPauseTrackUseCase: PlayPauseTrackUseCase,
    private val updateTrackPositionUseCase: UpdateTrackPositionUseCase,
    private val view: View
) {

    private var currentTrack: Track? = null

    interface View {
        fun showTrackInfo(track: Track)
        fun updatePlayPauseButton(isPlaying: Boolean)
        fun updateTrackPosition(position: String)
    }

    fun onTrackSelected(trackId: String) {
        currentTrack = getTrackUseCase.execute(trackId)
        currentTrack?.let {
            view.showTrackInfo(it)
            prepareTrackUseCase.execute(it.previewUrl, {
                view.updatePlayPauseButton(false)
            }, {
                view.updatePlayPauseButton(false)
            })
        }
    }

    fun onPlayPauseClicked() {
        currentTrack?.let {
            if (it.isPlaying) {
                playPauseTrackUseCase.pause()
                it.isPlaying = false
            } else {
                playPauseTrackUseCase.play()
                it.isPlaying = true
            }
            view.updatePlayPauseButton(it.isPlaying)
        }
    }

    fun updateTrackPosition() {
        val position = updateTrackPositionUseCase.execute()
        val formattedPosition = formatTime(position)
        view.updateTrackPosition(formattedPosition)
    }

    private fun formatTime(milliseconds: Int): String {
        val seconds = milliseconds / 1000
        return String.format("%d:%02d", seconds / 60, seconds % 60)
    }
}