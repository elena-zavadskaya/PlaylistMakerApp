package com.practicum.playlistmakerapp.search.data.Iimpl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.search.domain.repository.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SearchHistoryRepository {

    override fun addTrack(track: Track) {
        // Добавление трека в историю
    }

    override fun clearSearchHistory() {
        // Очистка истории поиска
    }

    override fun getTracks(): ArrayList<Track> {
        // Получение списка треков из истории
        return arrayListOf()
    }
}