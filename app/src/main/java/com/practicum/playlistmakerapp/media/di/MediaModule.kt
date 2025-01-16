package com.practicum.playlistmakerapp.media.di

import androidx.room.Room
import com.practicum.playlistmakerapp.create.data.db.PlaylistDatabase
import com.practicum.playlistmakerapp.create.data.impl.CreatePlaylistRepositoryImpl
import com.practicum.playlistmakerapp.create.domain.impl.CreatePlaylistInteractorImpl
import com.practicum.playlistmakerapp.create.domain.interactor.CreatePlaylistInteractor
import com.practicum.playlistmakerapp.create.domain.repository.CreatePlaylistRepository
import com.practicum.playlistmakerapp.create.ui.CreatePlaylistViewModel
import com.practicum.playlistmakerapp.media.ui.PlaylistViewModel
import com.practicum.playlistmakerapp.media.ui.favorites.FavoritesViewModel
import com.practicum.playlistmakerapp.media.ui.media.MediaLibraryViewModel
import com.practicum.playlistmakerapp.media.ui.playlists.PlaylistsViewModel
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

    factory<CreatePlaylistRepository> {
        CreatePlaylistRepositoryImpl(
            playlistDao = get(),
            playlistTrackDao = get(),
            context = get()
        )
    }
    factory<CreatePlaylistInteractor> { CreatePlaylistInteractorImpl(get()) }

    viewModel { CreatePlaylistViewModel(get()) }
    viewModel { PlaylistsViewModel(get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { MediaLibraryViewModel() }
    viewModel { PlaylistViewModel(get()) }
}
