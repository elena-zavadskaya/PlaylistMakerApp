package com.practicum.playlistmakerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmakerapp.databinding.ActivitySearchBinding
import com.practicum.playlistmakerapp.databinding.AudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: AudioPlayerBinding
    private lateinit var chosenTrack: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backButton.setOnClickListener {
            finish()
        }

        val chosenTrackJson = intent.extras?.getString("KEY")
        val gson = Gson()
        chosenTrack = gson.fromJson(chosenTrackJson, Track::class.java)

        val trackName: TextView = findViewById(R.id.trackTitleTV)
        val trackImage: ImageView = findViewById(R.id.albumIV)
        val artistName: TextView = findViewById(R.id.trackAuthorTV)
        val trackTimeMillis: TextView = findViewById(R.id.trackTimeMillisTV)
        val collectionName: TextView = findViewById(R.id.collectionNameTV)
        val releaseDate: TextView = findViewById(R.id.releaseDateTV)
        val primaryGenreName: TextView = findViewById(R.id.primaryGenreNameTV)
        val country: TextView = findViewById(R.id.countryTV)

        trackName.text = chosenTrack.trackName

        Glide.with(trackImage)
            .load(chosenTrack.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(DpToPx.dpToPx(2f, trackImage)))
            .into(trackImage)

        artistName.text = chosenTrack.artistName
        trackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(chosenTrack.trackTimeMillis.toLong())

        collectionName.text = chosenTrack.collectionName
        releaseDate.text = chosenTrack.releaseDate.substring(0,4)

        primaryGenreName.text = chosenTrack.primaryGenreName
        country.text = chosenTrack.country
    }
}