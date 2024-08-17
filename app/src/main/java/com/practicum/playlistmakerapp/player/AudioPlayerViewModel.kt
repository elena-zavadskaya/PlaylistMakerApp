package com.practicum.playlistmakerapp.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmakerapp.player.usecases.PauseTrackUseCase
import com.practicum.playlistmakerapp.player.usecases.PlayTrackUseCase
import com.practicum.playlistmakerapp.player.usecases.PrepareTrackUseCase
import com.practicum.playlistmakerapp.player.usecases.UpdateTrackPositionUseCase

class AudioPlayerViewModel(
    private val prepareTrackUseCase: PrepareTrackUseCase,
    private val playTrackUseCase: PlayTrackUseCase,
    private val pauseTrackUseCase: PauseTrackUseCase,
    private val updateTrackPositionUseCase: UpdateTrackPositionUseCase
) : ViewModel() {

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }

    private val playerState = MutableLiveData(0)

    private val trackPosition = MutableLiveData(0)

    fun prepareTrack(url: String) {
        prepareTrackUseCase.execute(PrepareTrackUseCase.Params(url, {
            playerState.postValue(STATE_PREPARED)
        }, {
            playerState.postValue(STATE_DEFAULT)
        }))

    }

    fun playTrack() {
        playTrackUseCase.play()
        playerState.postValue(STATE_PLAYING)
    }

    fun pauseTrack() {
        pauseTrackUseCase.pause()
        playerState.postValue(STATE_PAUSED)
    }

    fun updateTrackPosition() {
        trackPosition.postValue(updateTrackPositionUseCase.execute(Unit))
    }

    fun getPlayerStateLiveData(): LiveData<Int> = playerState

    fun getTrackPositionLiveData(): LiveData<Int> = trackPosition
}