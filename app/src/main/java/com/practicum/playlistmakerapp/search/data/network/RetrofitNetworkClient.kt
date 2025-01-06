package com.practicum.playlistmakerapp.search.data.network

import com.practicum.playlistmakerapp.search.data.dto.Response
import com.practicum.playlistmakerapp.search.data.dto.TracksSearchRequest

class RetrofitNetworkClient(
    private val iTunesService: ITunesApi
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            try {
                val response = iTunesService.trackSearch(dto.expression)
                response.apply { resultCode = 200 }
            } catch (e: Exception) {
                Response().apply { resultCode = 500 }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}