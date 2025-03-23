package com.practicum.playlistmakerapp.media.ui.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import com.practicum.playlistmakerapp.create.domain.interactor.CreatePlaylistInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val createPlaylistInteractor: CreatePlaylistInteractor
) : ViewModel() {

    private val _playlists = MutableStateFlow<List<PlaylistEntity>>(emptyList())
    val playlists: StateFlow<List<PlaylistEntity>> get() = _playlists

    fun loadPlaylists() {
        viewModelScope.launch {
            createPlaylistInteractor.getAllPlaylists().collect { playlistList ->
                _playlists.value = playlistList
            }
        }
    }
}

