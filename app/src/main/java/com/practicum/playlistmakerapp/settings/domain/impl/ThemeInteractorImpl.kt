package com.practicum.playlistmakerapp.settings.domain.impl

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmakerapp.settings.domain.interactor.ThemeInteractor
import com.practicum.playlistmakerapp.settings.domain.repository.ThemeRepository

class ThemeInteractorImpl(private val themeRepository: ThemeRepository) : ThemeInteractor {

    override fun isDarkThemeEnabled(): Boolean {
        return themeRepository.isDarkThemeEnabled()
    }

    override fun switchTheme(isDarkTheme: Boolean) {
        themeRepository.setDarkThemeEnabled(isDarkTheme)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
