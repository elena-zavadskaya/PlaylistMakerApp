package com.practicum.playlistmakerapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.back_button)
        val shareButton = findViewById<ImageView>(R.id.share_button)
        val supportButton = findViewById<ImageView>(R.id.support_button)
        val termsOfUseButton = findViewById<ImageView>(R.id.terms_of_use_button)

        backButton.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }

        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_message))
            startActivity(shareIntent)
        }

        supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.write_to_support_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.write_to_support_message))
                startActivity(this)
            }
        }

        termsOfUseButton.setOnClickListener {
            val webpage: Uri = Uri.parse(getString(R.string.terms_of_use_link))
            val termsOfUseIntent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(termsOfUseIntent)
        }
    }
}