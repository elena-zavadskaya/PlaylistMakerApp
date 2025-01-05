package com.practicum.playlistmakerapp.search.data.network

import com.practicum.playlistmakerapp.search.data.dto.Response
import com.practicum.playlistmakerapp.search.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    override suspend fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            try {
                val response = iTunesService.trackSearch(dto.expression)

                return response.apply { resultCode = 200 }
            } catch (e: Exception) {
                return Response().apply {
                    resultCode = 500
                }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}