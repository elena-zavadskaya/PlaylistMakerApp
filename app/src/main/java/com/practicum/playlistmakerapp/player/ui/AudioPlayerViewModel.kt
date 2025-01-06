package com.practicum.playlistmakerapp.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmakerapp.player.domain.interactor.AudioPlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }

    private val _playerState = MutableLiveData(STATE_DEFAULT)
    val playerState: LiveData<Int> get() = _playerState

    private val _trackPosition = MutableLiveData(0)
    val trackPosition: LiveData<Int> get() = _trackPosition

    private var updateJob: Job? = null

    fun prepareTrack(url: String) {
        audioPlayerInteractor.prepareTrack(url, onPrepared = {
            _playerState.postValue(STATE_PREPARED)
        }, onError = {
            _playerState.postValue(STATE_DEFAULT)
        })
    }

    fun observeTrackCompletion(onComplete: () -> Unit) {
        audioPlayerInteractor.observeTrackCompletion {
            onComplete()
        }
    }

    fun playTrack() {
        audioPlayerInteractor.playTrack()
        _playerState.postValue(STATE_PLAYING)
        startUpdatingTrackPosition()

        observeTrackCompletion {
            resetTrack()
        }
    }

    fun pauseTrack() {
        audioPlayerInteractor.pauseTrack()
        _playerState.postValue(STATE_PAUSED)
        stopUpdatingTrackPosition()
    }

    private fun resetTrack() {
        _playerState.postValue(STATE_DEFAULT)
        _trackPosition.postValue(0)
        stopUpdatingTrackPosition()
    }

    private fun startUpdatingTrackPosition() {
        stopUpdatingTrackPosition()
        updateJob = viewModelScope.launch {
            audioPlayerInteractor.observeTrackProgress().collect { position ->
                _trackPosition.postValue(position)
            }
        }
    }

    private fun stopUpdatingTrackPosition() {
        updateJob?.cancel()
        updateJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopUpdatingTrackPosition()
        audioPlayerInteractor.release()
    }
}