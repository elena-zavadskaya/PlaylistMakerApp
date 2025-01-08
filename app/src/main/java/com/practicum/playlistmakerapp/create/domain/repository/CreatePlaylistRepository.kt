package com.practicum.playlistmakerapp.create.domain.repository

import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import kotlinx.coroutines.flow.Flow

interface CreatePlaylistRepository {
    suspend fun savePlaylist(playlist: PlaylistEntity)
    suspend fun updatePlaylist(playlist: PlaylistEntity)
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
}
