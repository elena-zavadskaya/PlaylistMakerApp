package com.practicum.playlistmakerapp.settings.data.impl

import android.app.Application
import com.practicum.playlistmakerapp.App
import com.practicum.playlistmakerapp.settings.ThemeSettings
import com.practicum.playlistmakerapp.settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val application: Application
) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(isDarkThemeEnabled = (application as App).darkTheme)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        (application as App).switchTheme(settings.isDarkThemeEnabled)
    }
}