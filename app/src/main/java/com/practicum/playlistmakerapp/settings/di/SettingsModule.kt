package com.practicum.playlistmakerapp.settings.di

import com.practicum.playlistmakerapp.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmakerapp.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmakerapp.settings.domain.interactor.SettingsInteractor
import com.practicum.playlistmakerapp.settings.domain.repository.SettingsRepository
import com.practicum.playlistmakerapp.settings.ui.SettingsViewModel
import com.practicum.playlistmakerapp.sharing.data.ExternalNavigator
import com.practicum.playlistmakerapp.sharing.domain.SharingInteractor
import com.practicum.playlistmakerapp.sharing.domain.SharingInteractorImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {

    single<SettingsRepository> { SettingsRepositoryImpl(androidApplication()) }

    single<SettingsInteractor> { SettingsInteractorImpl(get()) }

    single<SharingInteractor> { SharingInteractorImpl(get(), get()) }

    single { ExternalNavigator(androidContext()) }

    viewModel { SettingsViewModel(get(), get()) }
}
