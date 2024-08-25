package com.practicum.playlistmakerapp.search.data.network

import com.practicum.playlistmakerapp.search.data.dto.TrackResponse
import com.practicum.playlistmakerapp.search.data.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun trackSearch(@Query("expression") expression: String): Call<TracksSearchResponse>
}