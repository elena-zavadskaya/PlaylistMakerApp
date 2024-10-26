package com.practicum.playlistmakerapp.media.ui

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {
    viewModel { PlaylistsViewModel() }
    viewModel { FavoritesViewModel() }
    viewModel { MediaLibraryViewModel() }
}
