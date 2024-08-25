package com.practicum.playlistmakerapp.search.usecases

import com.practicum.playlistmakerapp.search.data.network.ITunesApi
import com.practicum.playlistmakerapp.search.data.dto.TrackResponse
import com.practicum.playlistmakerapp.player.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchTracksUseCase(private val searchService: ITunesApi) {

    fun execute(query: String, callback: (Result<List<Track>>) -> Unit) {
        searchService.trackSearch(query).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.isSuccessful) {
                    val tracks = response.body()?.results ?: emptyList()
                    callback(Result.success(tracks))
                } else {
                    callback(Result.failure(Exception("Search failed with status code ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}