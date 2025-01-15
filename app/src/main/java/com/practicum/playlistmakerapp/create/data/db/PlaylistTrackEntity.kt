package com.practicum.playlistmakerapp.create.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_tracks")
data class PlaylistTrackEntity(
    @PrimaryKey val id: String,
    val coverUrl: String,
    val title: String,
    val artist: String,
    val album: String?,
    val releaseYear: Int,
    val genre: String,
    val country: String,
    val duration: String,
    val fileUrl: String
)