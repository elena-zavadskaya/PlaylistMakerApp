package com.practicum.playlistmakerapp.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmakerapp.App
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.databinding.ActivitySettingsBinding
import com.practicum.playlistmakerapp.main.ui.MainActivity
import com.practicum.playlistmakerapp.search.SearchViewModel
import com.practicum.playlistmakerapp.settings.SettingsViewModel

class SettingsActivity : ComponentActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]

        binding.themeSwitcher.isChecked = viewModel.themeState.value ?: false
        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }

        viewModel.themeState.observe(this) { isDarkTheme ->
            binding.themeSwitcher.isChecked = isDarkTheme
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, viewModel.getShareAppMessage())
            }
            startActivity(shareIntent)
        }

        binding.supportButton.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(viewModel.getSupportEmail()))
                putExtra(Intent.EXTRA_SUBJECT, viewModel.getSupportSubject())
                putExtra(Intent.EXTRA_TEXT, viewModel.getSupportMessage())
                startActivity(this)
            }
        }

        binding.termsOfUseButton.setOnClickListener {
            val webpage: Uri = Uri.parse(viewModel.getTermsOfUseLink())
            val termsOfUseIntent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(termsOfUseIntent)
        }
    }
}
