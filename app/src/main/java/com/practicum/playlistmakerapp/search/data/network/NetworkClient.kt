package com.practicum.playlistmakerapp.search.data.network

import com.practicum.playlistmakerapp.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}