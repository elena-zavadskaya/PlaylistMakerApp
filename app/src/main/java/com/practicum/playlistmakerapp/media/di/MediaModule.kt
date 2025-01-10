package com.practicum.playlistmakerapp.media.di

import androidx.room.Room
import com.practicum.playlistmakerapp.create.data.db.PlaylistDatabase
import com.practicum.playlistmakerapp.create.data.impl.CreatePlaylistRepositoryImpl
import com.practicum.playlistmakerapp.create.domain.impl.CreatePlaylistInteractorImpl
import com.practicum.playlistmakerapp.create.domain.interactor.CreatePlaylistInteractor
import com.practicum.playlistmakerapp.create.domain.repository.CreatePlaylistRepository
import com.practicum.playlistmakerapp.create.ui.CreatePlaylistViewModel
import com.practicum.playlistmakerapp.media.ui.FavoritesViewModel
import com.practicum.playlistmakerapp.media.ui.MediaLibraryViewModel
import com.practicum.playlistmakerapp.media.ui.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {
    single {
        Room.databaseBuilder(
            get(),
            PlaylistDatabase::class.java,
            "playlist_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<PlaylistDatabase>().playlistDao() }
    single { get<PlaylistDatabase>().playlistTrackDao() }

    factory<CreatePlaylistRepository> { CreatePlaylistRepositoryImpl(get(), get()) }
    factory<CreatePlaylistInteractor> { CreatePlaylistInteractorImpl(get()) }
    viewModel { CreatePlaylistViewModel(get()) }

    factory<CreatePlaylistRepository> { CreatePlaylistRepositoryImpl(get(), get()) }
    factory<CreatePlaylistInteractor> { CreatePlaylistInteractorImpl(get()) }
    viewModel { CreatePlaylistViewModel(get()) }
    viewModel { PlaylistsViewModel(get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { MediaLibraryViewModel() }
}
