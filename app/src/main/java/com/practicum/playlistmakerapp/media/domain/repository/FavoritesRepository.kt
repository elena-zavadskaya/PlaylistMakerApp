package com.practicum.playlistmakerapp.media.domain.repository

import com.practicum.playlistmakerapp.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun addTrackToFavorites(track: Track)
    suspend fun removeTrackFromFavorites(track: Track)
    fun getFavoriteTracks(): Flow<List<Track>>
}