package com.practicum.playlistmakerapp.create.data.impl

import com.practicum.playlistmakerapp.create.data.db.PlaylistDao
import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import com.practicum.playlistmakerapp.create.domain.repository.CreatePlaylistRepository
import kotlinx.coroutines.flow.Flow

class CreatePlaylistRepositoryImpl(private val playlistDao: PlaylistDao) : CreatePlaylistRepository {
    override suspend fun savePlaylist(playlist: PlaylistEntity) {
        playlistDao.insertPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: PlaylistEntity) {
        playlistDao.updatePlaylist(playlist)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistEntity>> {
        return playlistDao.getAllPlaylists()
    }
}


