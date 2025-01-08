package com.practicum.playlistmakerapp.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmakerapp.media.domain.interactor.FavoritesInteractor
import com.practicum.playlistmakerapp.player.domain.interactor.AudioPlayerInteractor
import com.practicum.playlistmakerapp.player.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor
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

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private var updateJob: Job? = null
    private lateinit var currentTrack: Track

    fun prepareTrack(track: Track) {
        currentTrack = track
        _isFavorite.value = track.isFavorite

        audioPlayerInteractor.prepareTrack(track.previewUrl, onPrepared = {
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

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (currentTrack.isFavorite) {
                favoritesInteractor.removeTrack(currentTrack)
            } else {
                favoritesInteractor.addTrack(currentTrack)
            }
            currentTrack.isFavorite = !currentTrack.isFavorite
            _isFavorite.postValue(currentTrack.isFavorite)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopUpdatingTrackPosition()
        audioPlayerInteractor.release()
    }
}