package com.practicum.playlistmakerapp.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import com.practicum.playlistmakerapp.create.domain.interactor.CreatePlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val createPlaylistInteractor: CreatePlaylistInteractor
) : ViewModel() {

    private val _playlist = MutableLiveData<PlaylistEntity?>()
    val playlist: LiveData<PlaylistEntity?> get() = _playlist

    private val _totalDuration = MutableLiveData<String>()
    val totalDuration: LiveData<String> get() = _totalDuration

    fun loadPlaylistById(playlistId: Long) {
        viewModelScope.launch {
            val result = createPlaylistInteractor.getPlaylistById(playlistId)
            _playlist.value = result
            result?.let {
                val trackIds = parseTrackIds(it.trackIds)
                calculateTotalDuration(trackIds)
            }
        }
    }

    private fun parseTrackIds(trackIdsJson: String): List<String> {
        return try {
            Gson().fromJson(trackIdsJson, Array<String>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun calculateTotalDuration(trackIds: List<String>) {
        viewModelScope.launch {
            val cleanedIds = trackIds.map { it.trim() }

            val tracks = createPlaylistInteractor.getTracksByIds(cleanedIds)

            var totalDurationInMillis = 0L
            for (track in tracks) {
                totalDurationInMillis += parseDuration(track.duration)
            }

            _totalDuration.value = formatDuration(totalDurationInMillis)
        }
    }

    private fun parseDuration(duration: String): Long {
        return try {
            val parts = duration.split(":")
            val minutes = parts[0].toLongOrNull() ?: 0L
            val seconds = parts[1].toLongOrNull() ?: 0L
            (minutes * 60 + seconds) * 1000
        } catch (e: Exception) {
            0L
        }
    }

    private fun formatDuration(durationInMillis: Long): String {
        val durationInMinutes = durationInMillis / 60000

        val minuteWord = when {
            durationInMinutes % 10 == 1L && durationInMinutes % 100 != 11L -> "минута"
            durationInMinutes % 10 in 2..4 && (durationInMinutes % 100 !in 12..14) -> "минуты"
            else -> "минут"
        }

        return "$durationInMinutes $minuteWord"
    }
}
