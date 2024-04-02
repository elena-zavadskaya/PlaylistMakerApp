package com.practicum.playlistmakerapp

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val SHARED_PREFERENCES_FOR_DARK_THEME = "shared_preference_for_dark_theme"
const val KEY_FOR_DARK_THEME = "key_for_dark_theme"

class App : Application() {

    var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(SHARED_PREFERENCES_FOR_DARK_THEME, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(KEY_FOR_DARK_THEME, false)
        switchTheme(darkTheme)

    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        saveDarkThemeState()
    }

    private fun saveDarkThemeState() {
        sharedPrefs.edit()
            .putBoolean(KEY_FOR_DARK_THEME, darkTheme)
            .apply()
    }
}