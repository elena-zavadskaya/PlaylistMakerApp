package com.practicum.playlistmakerapp.create.domain.impl

import com.google.gson.Gson
import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import com.practicum.playlistmakerapp.create.domain.interactor.CreatePlaylistInteractor
import com.practicum.playlistmakerapp.create.domain.repository.CreatePlaylistRepository

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
}
