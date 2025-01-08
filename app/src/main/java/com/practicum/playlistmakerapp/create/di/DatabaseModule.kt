package com.practicum.playlistmakerapp.create.di

import androidx.room.Room
import com.practicum.playlistmakerapp.create.data.db.PlaylistDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(), // контекст приложения
            PlaylistDatabase::class.java,
            "playlist_database"
        ).build()
    }
    single { get<PlaylistDatabase>().playlistDao() }
}
