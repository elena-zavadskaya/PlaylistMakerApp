package com.practicum.playlistmakerapp.create.domain.repository

import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: PlaylistEntity)
    suspend fun updatePlaylist(playlist: PlaylistEntity)
    suspend fun getPlaylistById(id: Int): PlaylistEntity?
}
