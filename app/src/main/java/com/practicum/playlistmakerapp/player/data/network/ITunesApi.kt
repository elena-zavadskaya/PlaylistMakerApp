package com.practicum.playlistmakerapp.player.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun trackSearch(@Query("term") text: String): Call<TrackResponse>
}