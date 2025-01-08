package com.practicum.playlistmakerapp.create.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlaylistEntity::class], version = 1)
abstract class PlaylistDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}