package com.practicum.playlistmakerapp.settings.domain.interactor

interface ThemeInteractor {
    fun isDarkThemeEnabled(): Boolean
    fun switchTheme(isDarkTheme: Boolean)
}
