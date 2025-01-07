package com.practicum.playlistmakerapp.media.data.impl

import com.practicum.playlistmakerapp.media.data.db.FavoritesDao
import com.practicum.playlistmakerapp.media.data.db.FavoritesEntity
import com.practicum.playlistmakerapp.media.domain.repository.FavoritesRepository
import com.practicum.playlistmakerapp.player.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val favoritesDao: FavoritesDao
) : FavoritesRepository {

    override suspend fun addTrackToFavorites(track: Track) {
        favoritesDao.insertTrack(track.toFavoritesEntity())
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        favoritesDao.deleteTrack(track.toFavoritesEntity())
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoritesDao.getAllTracksFlow().map { entities ->
            entities.map { it.toTrack() }
        }
    }
}

fun Track.toFavoritesEntity(): FavoritesEntity {
    return FavoritesEntity(
        id = trackId,
        coverUrl = artworkUrl100,
        title = trackName,
        artist = artistName,
        album = collectionName,
        releaseYear = releaseDate.take(4).toIntOrNull() ?: 0,
        genre = primaryGenreName,
        country = country,
        duration = trackTimeMillis,
        fileUrl = previewUrl
    )
}

fun FavoritesEntity.toTrack(): Track {
    return Track(
        trackId = id,
        trackName = title,
        artistName = artist,
        trackTimeMillis = duration,
        artworkUrl100 = coverUrl,
        collectionName = album.orEmpty(),
        releaseDate = releaseYear.toString(),
        primaryGenreName = genre,
        country = country,
        previewUrl = fileUrl,
        isFavorite = true
    )
}