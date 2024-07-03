package com.practicum.playlistmakerapp.data.api

import com.practicum.playlistmakerapp.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Интерфейс для работы с внешним API iTunes для поиска треков.
interface ITunesApi {
    @GET("/search?entity=song")
    fun trackSearch(@Query("term") text: String): Call<TrackResponse>
}