package com.practicum.playlistmakerapp.create.domain.interactor

import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import com.practicum.playlistmakerapp.create.data.db.PlaylistTrackEntity
import kotlinx.coroutines.flow.Flow

interface CreatePlaylistInteractor {
    // Методы для плейлистов
    suspend fun createPlaylist(name: String, description: String, coverImagePath: String)
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    // Методы для треков
    suspend fun addTrackToPlaylist(track: PlaylistTrackEntity)
    suspend fun getTrackById(id: String): PlaylistTrackEntity?
    suspend fun getAllTracks(): List<PlaylistTrackEntity>

}
