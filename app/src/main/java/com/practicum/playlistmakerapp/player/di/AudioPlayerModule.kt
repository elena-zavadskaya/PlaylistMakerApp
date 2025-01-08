package com.practicum.playlistmakerapp.player.di

import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmakerapp.player.data.impl.AudioPlayerImpl
import com.practicum.playlistmakerapp.player.data.impl.TrackRepositoryImpl
import com.practicum.playlistmakerapp.player.domain.interactor.AudioPlayerInteractor
import com.practicum.playlistmakerapp.player.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmakerapp.player.domain.repository.AudioPlayerRepository
import com.practicum.playlistmakerapp.player.domain.repository.TrackRepository
import com.practicum.playlistmakerapp.player.ui.AudioPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val audioPlayerModule = module {
    single { Gson() }

    factory { MediaPlayer() }

    factory<AudioPlayerRepository> { AudioPlayerImpl(get()) }

    single<TrackRepository> { TrackRepositoryImpl(get(), get()) }

    single<AudioPlayerInteractor> { AudioPlayerInteractorImpl(get()) }

    viewModel {
        AudioPlayerViewModel(get(), get())
    }
}
