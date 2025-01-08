package com.practicum.playlistmakerapp.media.di

import com.practicum.playlistmakerapp.media.domain.impl.FavoritesInteractorImpl
import com.practicum.playlistmakerapp.media.domain.interactor.FavoritesInteractor
import org.koin.dsl.module

val domainModule = module {
    single<FavoritesInteractor> { FavoritesInteractorImpl(get()) }
}
