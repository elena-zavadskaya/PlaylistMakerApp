package com.practicum.playlistmakerapp.media.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import com.practicum.playlistmakerapp.create.domain.repository.PlaylistRepository
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val repository: PlaylistRepository) : ViewModel() {

    fun createPlaylist(
        name: String,
        description: String?,
        coverImagePath: String?,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (name.isBlank()) {
            onError("Название плейлиста не может быть пустым")
            return
        }

        val playlist = PlaylistEntity(
            name = name,
            description = description,
            coverImagePath = coverImagePath ?: "",
            trackIds = PlaylistEntity.fromTrackIds(emptyList())
        )

        viewModelScope.launch {
            try {
                repository.addPlaylist(playlist)
                onSuccess(name)
            } catch (e: Exception) {
                onError("Ошибка при создании плейлиста")
            }
        }
    }
}
