package com.practicum.playlistmakerapp.search.data.dto

import com.practicum.playlistmakerapp.player.domain.models.Track

class TracksSearchResponse(val searchType: String,
                           val expression: String,
                           val results: List<Track>) : Response()