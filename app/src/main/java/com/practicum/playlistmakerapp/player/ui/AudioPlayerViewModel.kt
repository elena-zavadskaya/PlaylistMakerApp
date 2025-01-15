package com.practicum.playlistmakerapp.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import com.practicum.playlistmakerapp.create.data.db.PlaylistTrackEntity
import com.practicum.playlistmakerapp.create.domain.interactor.CreatePlaylistInteractor
import com.practicum.playlistmakerapp.media.domain.interactor.FavoritesInteractor
import com.practicum.playlistmakerapp.media.domain.model.Playlist
import com.practicum.playlistmakerapp.player.domain.interactor.AudioPlayerInteractor
import com.practicum.playlistmakerapp.player.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val createPlaylistInteractor: CreatePlaylistInteractor
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

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    private val _trackAddStatus = MutableLiveData<String?>()
    val trackAddStatus: LiveData<String?> get() = _trackAddStatus

    private val _closeBottomSheet = MutableLiveData<Boolean>()
    val closeBottomSheet: LiveData<Boolean> get() = _closeBottomSheet

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

    fun loadPlaylists() {
        viewModelScope.launch {
            createPlaylistInteractor.getAllPlaylists().collect { playlistEntities ->
                val playlistList = playlistEntities.map { entity ->
                    Playlist(
                        id = entity.id,
                        name = entity.name,
                        description = entity.description,
                        coverImagePath = entity.coverImagePath,
                        trackIds = entity.trackIds,
                        trackCount = entity.trackCount
                    )
                }
                _playlists.postValue(playlistList)
            }
        }
    }

    fun closeBottomSheet() {
        _closeBottomSheet.postValue(true)
    }

    fun resetBottomSheetCloseState() {
        _closeBottomSheet.postValue(false)
    }

    fun addTrackToPlaylist(track: PlaylistTrackEntity, playlistId: Long) {
        viewModelScope.launch {
            val playlist = _playlists.value?.find { it.id == playlistId }

            playlist?.let {
                val trackIds = Gson().fromJson(it.trackIds, Array<Long>::class.java).toList()

                if (trackIds.contains(track.id.toLong())) {
                    _trackAddStatus.postValue("Трек уже добавлен в плейлист ${it.name}")
                } else {
                    val updatedTrackIds = trackIds + track.id.toLong()
                    val updatedPlaylist = it.copy(
                        trackIds = Gson().toJson(updatedTrackIds),
                        trackCount = updatedTrackIds.size
                    )

                    createPlaylistInteractor.updatePlaylist(
                        PlaylistEntity(
                            id = playlist.id,
                            name = updatedPlaylist.name,
                            description = updatedPlaylist.description,
                            coverImagePath = updatedPlaylist.coverImagePath,
                            trackIds = updatedPlaylist.trackIds,
                            trackCount = updatedPlaylist.trackCount
                        )
                    )

                    createPlaylistInteractor.addTrackToPlaylist(track)
                    _trackAddStatus.postValue("Добавлено в плейлист ${it.name}")
                    closeBottomSheet()
                }
            } ?: run {
                _trackAddStatus.postValue("Плейлист не найден")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopUpdatingTrackPosition()
        audioPlayerInteractor.release()
    }
}