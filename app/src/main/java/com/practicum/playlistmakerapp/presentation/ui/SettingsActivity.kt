package com.practicum.playlistmakerapp.presentation.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.playlistmakerapp.App
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.themeSwitcher.isChecked = (applicationContext as App).darkTheme
        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked  ->
            (applicationContext as App).switchTheme(isChecked)
        }

        binding.backButton.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }

        binding.shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_message))
            startActivity(shareIntent)
        }

        binding.supportButton.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.write_to_support_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.write_to_support_message))
                startActivity(this)
            }
        }

        binding.termsOfUseButton.setOnClickListener {
            val webpage: Uri = Uri.parse(getString(R.string.terms_of_use_link))
            val termsOfUseIntent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(termsOfUseIntent)
        }
    }
}