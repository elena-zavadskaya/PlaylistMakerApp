package com.practicum.playlistmakerapp

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmakerapp.player.di.audioPlayerModule
import com.practicum.playlistmakerapp.search.di.searchModule
import com.practicum.playlistmakerapp.settings.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

const val SHARED_PREFERENCES_FOR_DARK_THEME = "shared_preference_for_dark_theme"
const val KEY_FOR_DARK_THEME = "key_for_dark_theme"

class App : Application() {

    var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(audioPlayerModule, settingsModule, searchModule))
        }

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