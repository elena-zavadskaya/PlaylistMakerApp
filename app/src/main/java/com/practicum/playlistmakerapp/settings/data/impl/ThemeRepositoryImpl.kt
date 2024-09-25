package com.practicum.playlistmakerapp.settings.data.impl

import android.content.SharedPreferences
import com.practicum.playlistmakerapp.settings.domain.repository.ThemeRepository

class ThemeRepositoryImpl(private val sharedPreferences: SharedPreferences) : ThemeRepository {

    override fun isDarkThemeEnabled(): Boolean {
        return sharedPreferences.getBoolean("dark_theme", false)
    }

    override fun setDarkThemeEnabled(isEnabled: Boolean) {
        sharedPreferences.edit().putBoolean("dark_theme", isEnabled).apply()
    }
}
