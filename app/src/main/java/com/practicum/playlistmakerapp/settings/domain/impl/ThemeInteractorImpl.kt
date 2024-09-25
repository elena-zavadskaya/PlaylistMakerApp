package com.practicum.playlistmakerapp.settings.domain.impl

import com.practicum.playlistmakerapp.App
import com.practicum.playlistmakerapp.settings.domain.interactor.ThemeInteractor

class ThemeInteractorImpl(private val app: App) : ThemeInteractor {

    override fun isDarkThemeEnabled(): Boolean {
        return app.darkTheme
    }

    override fun switchTheme(isDarkTheme: Boolean) {
        app.switchTheme(isDarkTheme)
    }
}
