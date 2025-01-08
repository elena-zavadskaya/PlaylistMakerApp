package com.practicum.playlistmakerapp.media.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoritesEntity(
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