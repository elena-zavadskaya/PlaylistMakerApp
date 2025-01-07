package com.practicum.playlistmakerapp.media.di

import com.practicum.playlistmakerapp.media.data.impl.FavoritesRepositoryImpl
import com.practicum.playlistmakerapp.media.domain.repository.FavoritesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }
}