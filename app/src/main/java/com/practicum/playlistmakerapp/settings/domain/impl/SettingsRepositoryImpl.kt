package com.practicum.playlistmakerapp.settings.domain.impl

import android.content.SharedPreferences
import com.practicum.playlistmakerapp.settings.ThemeSettings
import com.practicum.playlistmakerapp.settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        val isDarkTheme = sharedPreferences.getBoolean(PREF_KEY_IS_DARK_THEME, false)
        return ThemeSettings(isDarkThemeEnabled = isDarkTheme)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(PREF_KEY_IS_DARK_THEME, settings.isDarkThemeEnabled).apply()
    }

    companion object {
        private const val PREF_KEY_IS_DARK_THEME = "is_dark_theme"
    }
}