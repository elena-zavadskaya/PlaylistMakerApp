package com.practicum.playlistmakerapp.settings.domain.interactor

import com.practicum.playlistmakerapp.settings.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}