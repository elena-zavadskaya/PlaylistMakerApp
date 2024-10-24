package com.practicum.playlistmakerapp.settings.domain.impl

import com.practicum.playlistmakerapp.settings.ThemeSettings
import com.practicum.playlistmakerapp.settings.domain.interactor.SettingsInteractor
import com.practicum.playlistmakerapp.settings.domain.repository.SettingsRepository

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
) : SettingsInteractor {

    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}