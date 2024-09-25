package com.practicum.playlistmakerapp.settings.domain.impl

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmakerapp.App
import com.practicum.playlistmakerapp.settings.domain.interactor.ThemeInteractor
import com.practicum.playlistmakerapp.settings.domain.repository.ThemeRepository

class ThemeInteractorImpl(private val app: App) : ThemeInteractor {

    override fun isDarkThemeEnabled(): Boolean {
        return app.darkTheme
    }

    override fun switchTheme(isDarkTheme: Boolean) {
        app.switchTheme(isDarkTheme)
    }
}
