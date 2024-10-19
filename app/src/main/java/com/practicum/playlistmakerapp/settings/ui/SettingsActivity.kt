package com.practicum.playlistmakerapp.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.playlistmakerapp.databinding.ActivitySettingsBinding
import com.practicum.playlistmakerapp.settings.ThemeSettings
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val currentTheme = viewModel.settingsInteractor.getThemeSettings()
        binding.themeSwitcher.isChecked = currentTheme.isDarkThemeEnabled

        // Переключение темы
        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            val newTheme = ThemeSettings(isDarkThemeEnabled = isChecked)
            viewModel.switchTheme(newTheme)
        }

        // Поделиться приложением
        binding.shareButton.setOnClickListener {
            viewModel.shareApp()
        }

        // Написать в поддержку
        binding.supportButton.setOnClickListener {
            viewModel.openSupport()
        }

        // Условия использования
        binding.termsOfUseButton.setOnClickListener {
            viewModel.openTerms()
        }

        // Назад
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}

