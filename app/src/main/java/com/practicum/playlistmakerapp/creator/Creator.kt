package com.practicum.playlistmakerapp.creator

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.gson.Gson
import com.practicum.playlistmakerapp.player.ui.AudioPlayerViewModel
import com.practicum.playlistmakerapp.player.data.impl.AudioPlayerImpl
import com.practicum.playlistmakerapp.player.data.impl.TrackRepositoryImpl
import com.practicum.playlistmakerapp.player.domain.usecases.PauseTrackUseCase
import com.practicum.playlistmakerapp.player.domain.usecases.PlayTrackUseCase
import com.practicum.playlistmakerapp.player.domain.usecases.PrepareTrackUseCase
import com.practicum.playlistmakerapp.player.domain.usecases.UpdateTrackPositionUseCase
import com.practicum.playlistmakerapp.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmakerapp.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmakerapp.search.data.network.SearchRepositoryImpl
import com.practicum.playlistmakerapp.search.domain.SearchHistoryInteractor
import com.practicum.playlistmakerapp.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmakerapp.search.domain.api.SearchInteractor
import com.practicum.playlistmakerapp.search.domain.api.SearchRepository
import com.practicum.playlistmakerapp.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmakerapp.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmakerapp.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmakerapp.settings.domain.impl.SettingsRepositoryImpl
import com.practicum.playlistmakerapp.settings.ui.SettingsViewModel
import com.practicum.playlistmakerapp.sharing.domain.ExternalNavigator
import com.practicum.playlistmakerapp.sharing.domain.SharingInteractor
import com.practicum.playlistmakerapp.sharing.domain.SharingInteractorImpl

object Creator {
    private val gson = Gson()
    private val audioPlayer = AudioPlayerImpl()
    private val trackRepository = TrackRepositoryImpl(gson, audioPlayer)
    private val externalNavigator = ExternalNavigator()

    // Аудиоплеер
    fun provideAudioPlayerViewModel(): AudioPlayerViewModel {
        return AudioPlayerViewModel(
            prepareTrackUseCase = PrepareTrackUseCase(trackRepository),
            playTrackUseCase = PlayTrackUseCase(trackRepository),
            pauseTrackUseCase = PauseTrackUseCase(trackRepository),
            updateTrackPositionUseCase = UpdateTrackPositionUseCase(trackRepository)
        )
    }

    // Настройки
    private fun getSharingRepository(application: Application): SharingRepository {
        return SharingRepositoryImpl(application)
    }

    fun provideSharingInteractor(application: Application): SharingInteractor {
        return SharingInteractorImpl(externalNavigator)
    }

    private fun getSettingsRepository(application: Application): SettingsRepository {
        return SettingsRepositoryImpl(application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE))
    }

    fun provideSettingsInteractor(application: Application): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(application))
    }

    fun provideSettingsViewModel(application: Application): SettingsViewModel {
        return SettingsViewModel(
            sharingInteractor = provideSharingInteractor(application),
            settingsInteractor = provideSettingsInteractor(application)
        )
    }

    fun provideSettingsViewModelFactory(application: Application): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                provideSettingsViewModel(application)
            }
        }
    }

    // Поиск
    private fun getSearchRepository(): SearchRepository {
        return SearchRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(getSearchRepository())
    }

    // История поиска
    private fun getSearchHistoryRepository(application: Application): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        )
    }

    fun provideSearchHistoryInteractor(application: Application): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(application))
    }
}