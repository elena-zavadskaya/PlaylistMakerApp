package com.practicum.playlistmakerapp.create.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmakerapp.create.domain.repository.PlaylistRepository
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    application: Application,
    private val playlistRepository: PlaylistRepository
) : AndroidViewModel(application) {

    private val _stateLiveData = MutableLiveData<String>()
    val stateLiveData: LiveData<String> = _stateLiveData

    fun createPlaylist(name: String, description: String?, coverImagePath: String?) {
        viewModelScope.launch {
            try {
                val newPlaylist = Playlist(
                    name = name,
                    description = description ?: "",
                    coverImagePath = coverImagePath,
                    tracks = emptyList(),
                    trackCount = 0
                )
                playlistRepository.addPlaylist(newPlaylist)
                _stateLiveData.postValue("Плейлист \"$name\" создан")
            } catch (e: Exception) {
                _stateLiveData.postValue("Ошибка при создании плейлиста")
            }
        }
    }
}