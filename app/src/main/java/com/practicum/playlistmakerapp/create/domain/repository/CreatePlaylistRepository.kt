package com.practicum.playlistmakerapp.create.domain.repository

import android.net.Uri
import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import com.practicum.playlistmakerapp.create.data.db.PlaylistTrackEntity
import com.practicum.playlistmakerapp.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface CreatePlaylistRepository {
    // Методы для плейлистов
    suspend fun savePlaylist(playlist: PlaylistEntity)
    suspend fun updatePlaylist(playlist: PlaylistEntity)
    suspend fun getPlaylistById(id: Long): PlaylistEntity?
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    // Методы для треков
    suspend fun addTrackToPlaylist(track: PlaylistTrackEntity)
    suspend fun getTrackById(id: String): PlaylistTrackEntity?
    suspend fun getAllTracks(): List<PlaylistTrackEntity>

    // Работа с файлами
    suspend fun saveImageToStorage(uri: Uri, fileName: String): Uri
    suspend fun getTracksByIds(ids: List<String>): List<Track>
    suspend fun deleteTrackFromPlaylist(trackId: String)
    suspend fun deletePlaylist(playlistId: Long)
}

