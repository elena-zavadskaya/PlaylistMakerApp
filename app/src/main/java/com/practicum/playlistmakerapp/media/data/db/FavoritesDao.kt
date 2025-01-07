package com.practicum.playlistmakerapp.media.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoritesEntity)

    @Delete
    suspend fun deleteTrack(track: FavoritesEntity)

    @Query("SELECT * FROM favorites ORDER BY rowid DESC")
    suspend fun getAllTracks(): List<FavoritesEntity>

    @Query("SELECT * FROM favorites ORDER BY rowid DESC")
    fun getAllTracksFlow(): Flow<List<FavoritesEntity>>
}
