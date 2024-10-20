package com.practicum.playlistmakerapp.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.practicum.playlistmakerapp.R
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

        val toolbar: Toolbar = findViewById(R.id.backbutton_toolbar)

        viewModel.currentTheme.observe(this) { themeSettings ->
            binding.themeSwitcher.isChecked = themeSettings.isDarkThemeEnabled
        }

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
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}

