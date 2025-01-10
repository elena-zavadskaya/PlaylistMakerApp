package com.practicum.playlistmakerapp.media.data.db.favorites

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoritesEntity::class], version = 1)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}