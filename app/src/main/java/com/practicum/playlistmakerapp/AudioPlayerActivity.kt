package com.practicum.playlistmakerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.playlistmakerapp.databinding.ActivitySearchBinding
import com.practicum.playlistmakerapp.databinding.AudioPlayerBinding

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: AudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}