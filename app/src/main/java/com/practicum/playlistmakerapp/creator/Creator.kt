package com.practicum.playlistmakerapp.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmakerapp.App
import com.practicum.playlistmakerapp.player.AudioPlayerViewModel
import com.practicum.playlistmakerapp.player.data.impl.AudioPlayerImpl
import com.practicum.playlistmakerapp.player.data.impl.TrackRepositoryImpl
import com.practicum.playlistmakerapp.player.domain.usecases.PauseTrackUseCase
import com.practicum.playlistmakerapp.player.domain.usecases.PlayTrackUseCase
import com.practicum.playlistmakerapp.player.domain.usecases.PrepareTrackUseCase
import com.practicum.playlistmakerapp.player.domain.usecases.UpdateTrackPositionUseCase
import com.practicum.playlistmakerapp.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmakerapp.search.data.network.TracksRepositoryImpl
import com.practicum.playlistmakerapp.search.domain.api.TracksInteractor
import com.practicum.playlistmakerapp.search.domain.api.TracksRepository
import com.practicum.playlistmakerapp.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmakerapp.settings.SettingsViewModel
import com.practicum.playlistmakerapp.settings.data.impl.ShareRepositoryImpl
import com.practicum.playlistmakerapp.settings.data.impl.SupportRepositoryImpl
import com.practicum.playlistmakerapp.settings.data.impl.TermsRepositoryImpl
import com.practicum.playlistmakerapp.settings.data.impl.ThemeRepositoryImpl
import com.practicum.playlistmakerapp.settings.domain.impl.ShareInteractorImpl
import com.practicum.playlistmakerapp.settings.domain.impl.SupportInteractorImpl
import com.practicum.playlistmakerapp.settings.domain.impl.TermsInteractorImpl
import com.practicum.playlistmakerapp.settings.domain.impl.ThemeInteractorImpl
import com.practicum.playlistmakerapp.settings.domain.interactor.ShareInteractor
import com.practicum.playlistmakerapp.settings.domain.interactor.SupportInteractor
import com.practicum.playlistmakerapp.settings.domain.interactor.TermsInteractor
import com.practicum.playlistmakerapp.settings.domain.interactor.ThemeInteractor
import com.practicum.playlistmakerapp.settings.domain.repository.ShareRepository
import com.practicum.playlistmakerapp.settings.domain.repository.SupportRepository
import com.practicum.playlistmakerapp.settings.domain.repository.TermsRepository
import com.practicum.playlistmakerapp.settings.domain.repository.ThemeRepository

object Creator {
    private val gson = Gson()
    private val audioPlayer = AudioPlayerImpl()
    private val trackRepository = TrackRepositoryImpl(gson, audioPlayer)

    fun provideAudioPlayerViewModel(): AudioPlayerViewModel {
        return AudioPlayerViewModel(
            prepareTrackUseCase = PrepareTrackUseCase(trackRepository),
            playTrackUseCase = PlayTrackUseCase(trackRepository),
            pauseTrackUseCase = PauseTrackUseCase(trackRepository),
            updateTrackPositionUseCase = UpdateTrackPositionUseCase(trackRepository)
        )
    }

    // Поиск
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    // Сменить тему
    private fun getSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

//    fun provideThemeRepository(application: Application): ThemeRepository {
//        return ThemeRepositoryImpl(getSharedPreferences(application))
//    }

    fun provideThemeInteractor(application: Application): ThemeInteractor {
        return ThemeInteractorImpl(application as App)
    }

    // Поделиться
    private fun getShareRepository(application: Application): ShareRepository {
        return ShareRepositoryImpl(application)
    }

    fun provideShareInteractor(application: Application): ShareInteractor {
        return ShareInteractorImpl(getShareRepository(application))
    }

    // Поддержка
    private fun getSupportRepository(application: Application): SupportRepository {
        return SupportRepositoryImpl(application)
    }

    fun provideSupportInteractor(application: Application): SupportInteractor {
        return SupportInteractorImpl(getSupportRepository(application))
    }

    // Условия использования
    private fun getTermsRepository(application: Application): TermsRepository {
        return TermsRepositoryImpl(application)
    }

    fun provideTermsInteractor(application: Application): TermsInteractor {
        return TermsInteractorImpl(getTermsRepository(application))
    }
}