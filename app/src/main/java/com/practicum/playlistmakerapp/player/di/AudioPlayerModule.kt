package com.practicum.playlistmakerapp.player.di

import com.google.gson.Gson
import com.practicum.playlistmakerapp.player.data.impl.AudioPlayerImpl
import com.practicum.playlistmakerapp.player.data.impl.TrackRepositoryImpl
import com.practicum.playlistmakerapp.player.domain.repository.AudioPlayerRepository
import com.practicum.playlistmakerapp.player.domain.repository.TrackRepository
import com.practicum.playlistmakerapp.player.domain.usecases.PauseTrackUseCase
import com.practicum.playlistmakerapp.player.domain.usecases.PlayTrackUseCase
import com.practicum.playlistmakerapp.player.domain.usecases.PrepareTrackUseCase
import com.practicum.playlistmakerapp.player.domain.usecases.UpdateTrackPositionUseCase
import com.practicum.playlistmakerapp.player.ui.AudioPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val audioPlayerModule = module {

    single { Gson() }

    single<AudioPlayerRepository> { AudioPlayerImpl() }

    single<TrackRepository> { TrackRepositoryImpl(get(), get()) } // Gson и AudioPlayer

    factory { PrepareTrackUseCase(get()) }
    factory { PlayTrackUseCase(get()) }
    factory { PauseTrackUseCase(get()) }
    factory { UpdateTrackPositionUseCase(get()) }

    viewModel {
        AudioPlayerViewModel(
            get(), // PrepareTrackUseCase
            get(), // PlayTrackUseCase
            get(), // PauseTrackUseCase
            get()  // UpdateTrackPositionUseCase
        )
    }
}