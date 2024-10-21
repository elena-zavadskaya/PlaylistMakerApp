package com.practicum.playlistmakerapp.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmakerapp.player.domain.interactor.AudioPlayerInteractor

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor
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
        audioPlayerInteractor.prepareTrack(url, {
            playerState.postValue(STATE_PREPARED)
        }, {
            playerState.postValue(STATE_DEFAULT)
        })
    }

    fun playTrack() {
        audioPlayerInteractor.playTrack()
        playerState.postValue(STATE_PLAYING)
    }

    fun pauseTrack() {
        audioPlayerInteractor.pauseTrack()
        playerState.postValue(STATE_PAUSED)
    }

    fun updateTrackPosition() {
        trackPosition.postValue(audioPlayerInteractor.getTrackPosition())
    }

    fun getPlayerStateLiveData(): LiveData<Int> = playerState
    fun getTrackPositionLiveData(): LiveData<Int> = trackPosition

    override fun onCleared() {
        super.onCleared()
        audioPlayerInteractor.release()
    }
}

