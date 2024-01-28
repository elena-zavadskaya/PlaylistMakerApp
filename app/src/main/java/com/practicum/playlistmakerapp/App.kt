package com.practicum.playlistmakerapp

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val SHARED_PREFERENCES = "shared_preference"
const val KEY = "key"

class App : Application() {

    private var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(KEY, false)
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
        sharedPrefs.edit() // состояние редактирования
            .putBoolean(KEY, darkTheme) // кладем новое значение
            .apply() // завершаем передачу данных
    }
}