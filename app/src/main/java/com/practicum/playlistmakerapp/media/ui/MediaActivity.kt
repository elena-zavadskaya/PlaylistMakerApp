package com.practicum.playlistmakerapp.media.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.playlistmakerapp.R

class MediaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MediaLibraryFragment())
                .commit()
        }
    }
}