package com.practicum.playlistmakerapp.create.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackToPlaylist(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_tracks WHERE id = :id")
    suspend fun getTrackById(id: String): PlaylistTrackEntity?

    @Query("SELECT * FROM playlist_tracks")
    suspend fun getAllTracks(): List<PlaylistTrackEntity>

    @Query("DELETE FROM playlist_tracks WHERE id = :id")
    suspend fun deleteTrack(id: String)
}