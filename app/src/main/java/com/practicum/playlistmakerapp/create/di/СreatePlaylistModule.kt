package com.practicum.playlistmakerapp.create.di

import com.practicum.playlistmakerapp.create.data.impl.CreatePlaylistRepositoryImpl
import com.practicum.playlistmakerapp.create.domain.impl.CreatePlaylistInteractorImpl
import com.practicum.playlistmakerapp.create.domain.interactor.CreatePlaylistInteractor
import com.practicum.playlistmakerapp.create.domain.repository.CreatePlaylistRepository
import com.practicum.playlistmakerapp.create.ui.CreatePlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val createPlaylistModule = module {
    factory<CreatePlaylistRepository> { CreatePlaylistRepositoryImpl(get()) } // Замените на вашу реализацию
    factory<CreatePlaylistInteractor> { CreatePlaylistInteractorImpl(get()) }
    viewModel { CreatePlaylistViewModel(get()) }
}
