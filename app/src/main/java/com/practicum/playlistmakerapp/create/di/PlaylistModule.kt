package com.practicum.playlistmakerapp.create.di

import android.app.Application
import com.practicum.playlistmakerapp.create.data.impl.PlaylistRepositoryImpl
import com.practicum.playlistmakerapp.create.domain.repository.PlaylistRepository
import com.practicum.playlistmakerapp.create.ui.CreatePlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistModule = module {

    single<PlaylistRepository> { PlaylistRepositoryImpl(get()) }

    viewModel { (application: Application) ->
        CreatePlaylistViewModel(application, get())
    }
}