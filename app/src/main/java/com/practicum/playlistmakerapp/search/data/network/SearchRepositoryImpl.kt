package com.practicum.playlistmakerapp.search.data.network

import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.search.data.dto.TracksSearchRequest
import com.practicum.playlistmakerapp.search.data.dto.TracksSearchResponse
import com.practicum.playlistmakerapp.search.domain.api.SearchRepository

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return if (response.resultCode == 200) {
            (response as TracksSearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        } else {
            emptyList()
        }
    }
}

