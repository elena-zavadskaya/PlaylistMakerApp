package com.practicum.playlistmakerapp.create.domain.interactor

import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import kotlinx.coroutines.flow.Flow

interface CreatePlaylistInteractor {
    suspend fun createPlaylist(name: String, description: String, coverImagePath: String)
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
}
