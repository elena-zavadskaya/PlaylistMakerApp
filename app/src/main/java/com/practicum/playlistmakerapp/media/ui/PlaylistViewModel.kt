package com.practicum.playlistmakerapp.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import com.practicum.playlistmakerapp.create.data.db.PlaylistTrackEntity
import com.practicum.playlistmakerapp.create.domain.interactor.CreatePlaylistInteractor
import com.practicum.playlistmakerapp.player.domain.models.Track
import kotlinx.coroutines.launch

sealed class PlaylistState {
    object Empty : PlaylistState()
    data class Loaded(val tracks: List<Track>, val totalDuration: String) : PlaylistState()
}

class PlaylistViewModel(
    private val createPlaylistInteractor: CreatePlaylistInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlaylistState>()
    val state: LiveData<PlaylistState> get() = _state

    private val _playlist = MutableLiveData<PlaylistEntity?>()
    val playlist: LiveData<PlaylistEntity?> get() = _playlist

    fun loadPlaylistById(playlistId: Long) {
        viewModelScope.launch {
            val result = createPlaylistInteractor.getPlaylistById(playlistId)
            _playlist.value = result
            result?.let {
                val trackIds = parseTrackIds(it.trackIds)
                loadTracks(trackIds)
            }
        }
    }

    private fun loadTracks(trackIds: List<String>) {
        viewModelScope.launch {
            val cleanedIds = trackIds.map { it.trim() }
            val tracks = createPlaylistInteractor.getTracksByIds(cleanedIds)
            val totalDuration = calculateTotalDuration(tracks)
            _state.postValue(
                if (tracks.isEmpty()) PlaylistState.Empty
                else PlaylistState.Loaded(tracks, totalDuration)
            )
        }
    }

    private fun parseTrackIds(trackIdsJson: String): List<String> {
        return try {
            Gson().fromJson(trackIdsJson, Array<String>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun calculateTotalDuration(tracks: List<Track>): String {
        if (tracks.isEmpty()) {
            return "0 минут"
        }
        val totalMillis = tracks.sumOf { parseDuration(it.trackTimeMillis) }
        return formatDuration(totalMillis)
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

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            createPlaylistInteractor.deleteTrackFromPlaylist(track.trackId)
            _playlist.value?.id?.let { loadPlaylistById(it) }
        }
    }

}