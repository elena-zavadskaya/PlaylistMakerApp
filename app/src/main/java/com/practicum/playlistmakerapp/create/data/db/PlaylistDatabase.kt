package com.practicum.playlistmakerapp.create.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlaylistEntity::class, PlaylistTrackEntity::class], version = 2)
abstract class PlaylistDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistTrackDao(): PlaylistTrackDao
}