package com.practicum.playlistmakerapp.settings.ui

import androidx.lifecycle.ViewModel
import com.practicum.playlistmakerapp.settings.ThemeSettings
import com.practicum.playlistmakerapp.settings.domain.interactor.SettingsInteractor
import com.practicum.playlistmakerapp.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    init {
        // Начальная тема
        val currentTheme = settingsInteractor.getThemeSettings()
        switchTheme(currentTheme)
    }

    // Сменить тему
    fun switchTheme(isDarkThemeEnabled: ThemeSettings) {
        settingsInteractor.updateThemeSetting(isDarkThemeEnabled)
    }

    // Поделиться приложением
    fun shareApp() {
        sharingInteractor.shareApp()
    }

    // Открыть правила использования
    fun openTerms() {
        sharingInteractor.openTerms()
    }

    // Написать в поддержку
    fun openSupport() {
        sharingInteractor.openSupport()
    }
}
