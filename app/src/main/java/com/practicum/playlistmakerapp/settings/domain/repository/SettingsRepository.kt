package com.practicum.playlistmakerapp.settings.domain.repository

import com.practicum.playlistmakerapp.settings.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}