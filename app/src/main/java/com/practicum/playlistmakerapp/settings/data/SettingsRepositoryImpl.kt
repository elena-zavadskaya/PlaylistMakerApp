package com.practicum.playlistmakerapp.settings.data

import android.content.SharedPreferences
import com.practicum.playlistmakerapp.App
import com.practicum.playlistmakerapp.settings.ThemeSettings
import com.practicum.playlistmakerapp.settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val app: App
) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(isDarkThemeEnabled = app.darkTheme)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        app.switchTheme(settings.isDarkThemeEnabled)
    }

}