package com.practicum.playlistmakerapp.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmakerapp.settings.ThemeSettings
import com.practicum.playlistmakerapp.settings.domain.interactor.SettingsInteractor
import com.practicum.playlistmakerapp.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private val _currentTheme = MutableLiveData<ThemeSettings>()
    val currentTheme: LiveData<ThemeSettings> = _currentTheme

    init {
        _currentTheme.value = settingsInteractor.getThemeSettings()
    }

    // Сменить тему
    fun switchTheme(isDarkThemeEnabled: ThemeSettings) {
        settingsInteractor.updateThemeSetting(isDarkThemeEnabled)
        _currentTheme.value = isDarkThemeEnabled
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
