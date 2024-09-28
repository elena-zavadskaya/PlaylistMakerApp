package com.practicum.playlistmakerapp.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmakerapp.creator.Creator
import com.practicum.playlistmakerapp.databinding.ActivitySettingsBinding
import com.practicum.playlistmakerapp.settings.ThemeSettings

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this, Creator.provideSettingsViewModelFactory())[SettingsViewModel::class.java]

        viewModel.themeSettings.observe(this) { themeSettings ->
            binding.themeSwitcher.isChecked = themeSettings.isDarkThemeEnabled
        }

        // Переключение темы
        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(ThemeSettings(isChecked))
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
