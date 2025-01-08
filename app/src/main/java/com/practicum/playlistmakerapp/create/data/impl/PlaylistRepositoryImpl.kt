package com.practicum.playlistmakerapp.create.data.impl

import com.practicum.playlistmakerapp.create.data.db.PlaylistDao
import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import com.practicum.playlistmakerapp.create.domain.repository.PlaylistRepository

class PlaylistRepositoryImpl(private val playlistDao: PlaylistDao) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: PlaylistEntity) {
        playlistDao.insertPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: PlaylistEntity) {
        playlistDao.updatePlaylist(playlist)
    }

    override suspend fun getPlaylistById(id: Int): PlaylistEntity? {
        return playlistDao.getPlaylistById(id)
    }
}
