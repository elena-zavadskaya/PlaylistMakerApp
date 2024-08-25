package com.practicum.playlistmakerapp.search

import com.practicum.playlistmakerapp.player.domain.models.Track

sealed class TracksState {

    object Loading : TracksState()

    data class Content(val tracks: List<Track>) : TracksState()

    object Empty : TracksState()

    object Error : TracksState()
}
