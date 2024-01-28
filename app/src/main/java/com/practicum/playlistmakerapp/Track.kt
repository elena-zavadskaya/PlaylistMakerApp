package com.practicum.playlistmakerapp

import com.google.gson.annotations.SerializedName

data class Track(
    val id: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String
)