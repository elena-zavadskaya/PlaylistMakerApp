package com.practicum.playlistmakerapp.media.di

import com.practicum.playlistmakerapp.media.ui.FavoritesViewModel
import com.practicum.playlistmakerapp.media.ui.MediaLibraryViewModel
import com.practicum.playlistmakerapp.media.ui.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {
    viewModel { PlaylistsViewModel() }
    viewModel { FavoritesViewModel(get()) }
    viewModel { MediaLibraryViewModel() }
}
