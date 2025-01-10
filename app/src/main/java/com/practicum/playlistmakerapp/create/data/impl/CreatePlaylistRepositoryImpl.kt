package com.practicum.playlistmakerapp.create.data.impl

import com.practicum.playlistmakerapp.create.data.db.PlaylistDao
import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import com.practicum.playlistmakerapp.create.data.db.PlaylistTrackDao
import com.practicum.playlistmakerapp.create.data.db.PlaylistTrackEntity
import com.practicum.playlistmakerapp.create.domain.repository.CreatePlaylistRepository
import kotlinx.coroutines.flow.Flow

class CreatePlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistTrackDao: PlaylistTrackDao
) : CreatePlaylistRepository {
    override suspend fun savePlaylist(playlist: PlaylistEntity) {
        playlistDao.insertPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: PlaylistEntity) {
        playlistDao.updatePlaylist(playlist)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistEntity>> {
        return playlistDao.getAllPlaylists()
    }

    override suspend fun addTrackToPlaylist(track: PlaylistTrackEntity) {
        playlistTrackDao.insertTrackToPlaylist(track)
    }

    override suspend fun getTrackById(id: String): PlaylistTrackEntity? {
        return playlistTrackDao.getTrackById(id)
    }

    override suspend fun getAllTracks(): List<PlaylistTrackEntity> {
        return playlistTrackDao.getAllTracks()
    }
}


