package com.practicum.playlistmakerapp.media.di

import android.content.Context
import androidx.room.Room
import com.practicum.playlistmakerapp.media.data.db.favorites.FavoritesDatabase
import org.koin.dsl.module

val favoritesDataModule = module {
    single { provideDatabase(get()) }
    single { get<FavoritesDatabase>().favoritesDao() }
}

fun provideDatabase(context: Context): FavoritesDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        FavoritesDatabase::class.java,
        "favorites_database"
    ).build()
}