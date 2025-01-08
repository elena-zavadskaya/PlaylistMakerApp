package com.practicum.playlistmakerapp.create.domain.interactor

interface CreatePlaylistInteractor {
    suspend fun createPlaylist(name: String, description: String, coverImagePath: String)
}
