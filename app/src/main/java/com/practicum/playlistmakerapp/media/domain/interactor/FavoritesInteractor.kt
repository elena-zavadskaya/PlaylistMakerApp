package com.practicum.playlistmakerapp.media.domain.interactor

import com.practicum.playlistmakerapp.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun addTrack(track: Track)
    suspend fun removeTrack(track: Track)
    fun getFavoriteTracks(): Flow<List<Track>>
}