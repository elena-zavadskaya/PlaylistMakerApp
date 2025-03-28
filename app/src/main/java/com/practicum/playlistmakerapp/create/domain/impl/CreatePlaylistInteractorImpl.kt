package com.practicum.playlistmakerapp.create.domain.impl

import android.net.Uri
import com.google.gson.Gson
import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import com.practicum.playlistmakerapp.create.data.db.PlaylistTrackEntity
import com.practicum.playlistmakerapp.create.domain.interactor.CreatePlaylistInteractor
import com.practicum.playlistmakerapp.create.domain.repository.CreatePlaylistRepository
import com.practicum.playlistmakerapp.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

class CreatePlaylistInteractorImpl(
    private val playlistRepository: CreatePlaylistRepository
) : CreatePlaylistInteractor {

    override suspend fun createPlaylist(name: String, description: String, coverImagePath: String) {
        val playlist = PlaylistEntity(
            name = name,
            description = description,
            coverImagePath = coverImagePath,
            trackIds = Gson().toJson(emptyList<Long>()),
            trackCount = 0
        )
        playlistRepository.savePlaylist(playlist)
    }

    override suspend fun getPlaylistById(id: Long): PlaylistEntity? {
        return playlistRepository.getPlaylistById(id)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistEntity>> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun updatePlaylist(playlist: PlaylistEntity) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        playlistRepository.deletePlaylist(playlistId)
    }

    override suspend fun addTrackToPlaylist(track: PlaylistTrackEntity) {
        playlistRepository.addTrackToPlaylist(track)
    }

    override suspend fun getTrackById(id: String): PlaylistTrackEntity? {
        return playlistRepository.getTrackById(id)
    }

    override suspend fun getAllTracks(): List<PlaylistTrackEntity> {
        return playlistRepository.getAllTracks()
    }

    override suspend fun getTracksByIds(ids: List<String>): List<Track> {
        return playlistRepository.getTracksByIds(ids)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String) {
        playlistRepository.deleteTrackFromPlaylist(trackId)
    }

    override suspend fun saveImage(uri: Uri): Uri {
        val fileName = "cover_${System.currentTimeMillis()}.jpg"
        return playlistRepository.saveImageToStorage(uri, fileName)
    }
}