package com.practicum.playlistmakerapp.media.domain.impl

import com.practicum.playlistmakerapp.media.domain.interactor.FavoritesInteractor
import com.practicum.playlistmakerapp.media.domain.repository.FavoritesRepository
import com.practicum.playlistmakerapp.player.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesInteractorImpl(
    private val repository: FavoritesRepository
) : FavoritesInteractor {

    override suspend fun addTrack(track: Track) {
        repository.addTrackToFavorites(track)
    }

    override suspend fun removeTrack(track: Track) {
        repository.removeTrackFromFavorites(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return repository.getFavoriteTracks().map { tracks ->
            tracks.sortedByDescending { it.trackId }
        }
    }
}