package com.practicum.playlistmakerapp.create.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String?,
    val coverImagePath: String,
    val trackIds: String,
    val trackCount: Int = 0
) {
    companion object {
        fun fromTrackIds(trackIds: List<String>): String = Gson().toJson(trackIds)
        fun toTrackIds(json: String): List<String> = Gson().fromJson(json, Array<String>::class.java).toList()
    }
}