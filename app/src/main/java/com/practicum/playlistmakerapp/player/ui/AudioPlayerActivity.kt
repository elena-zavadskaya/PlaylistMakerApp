package com.practicum.playlistmakerapp.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.databinding.AudioPlayerBinding
import com.practicum.playlistmakerapp.DpToPx
import com.practicum.playlistmakerapp.player.domain.models.Track
import org.koin.android.ext.android.inject

class AudioPlayerActivity : AppCompatActivity() {

    private val viewModel: AudioPlayerViewModel by inject()
    private val gson: Gson by inject()
    private lateinit var binding: AudioPlayerBinding
    private lateinit var chosenTrack: Track

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateUI(AudioPlayerViewModel.STATE_DEFAULT)

        intent.getStringExtra("KEY")?.also {
            chosenTrack = gson.fromJson(it, Track::class.java)
        }

        viewModel.prepareTrack(chosenTrack)

        binding.backButton.setOnClickListener {
            finish()
        }

        bindTrackInfo()

        binding.playIV.setOnClickListener {
            viewModel.playTrack()
        }

        binding.pauseIV.setOnClickListener {
            viewModel.pauseTrack()
        }

        binding.addToFavoritesIV.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        viewModel.playerState.observe(this) { state ->
            updateUI(state)
        }

        viewModel.trackPosition.observe(this, Observer { position ->
            Log.d("AudioPlayer", "Track position: $position")
            binding.durationTV.text = formatTime(position)
        })

        viewModel.playerState.observe(this) { state ->
            updateUI(state)
            if (state == AudioPlayerViewModel.STATE_DEFAULT) {
                binding.durationTV.text = "00:00"
            }
        }

        viewModel.isFavorite.observe(this) { isFavorite ->
            val favoriteIcon = if (isFavorite) R.drawable.added_to_favorites else R.drawable.add_to_favorites
            binding.addToFavoritesIV.setImageResource(favoriteIcon)
        }
    }

    private fun bindTrackInfo() {
        binding.trackTitleTV.text = chosenTrack.trackName
        Glide.with(binding.albumIV)
            .load(chosenTrack.getCoverArtwork())
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(DpToPx.dpToPx(2f, binding.albumIV)))
            .into(binding.albumIV)

        binding.trackAuthorTV.text = chosenTrack.artistName
        binding.trackTimeMillisTV.text = formatTime(chosenTrack.trackTimeMillis.toInt())
        binding.collectionNameTV.text = chosenTrack.collectionName
        binding.releaseDateTV.text = chosenTrack.releaseDate.substring(0, 4)
        binding.primaryGenreNameTV.text = chosenTrack.primaryGenreName
        binding.countryTV.text = chosenTrack.country
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseTrack()
    }

    private fun updateUI(state: Int) {
        Log.d("AudioPlayer", "Updating UI with state: $state")
        when (state) {
            AudioPlayerViewModel.STATE_PLAYING -> {
                binding.playIV.visibility = View.INVISIBLE
                binding.pauseIV.visibility = View.VISIBLE
            }
            AudioPlayerViewModel.STATE_PAUSED, AudioPlayerViewModel.STATE_PREPARED,
            AudioPlayerViewModel.STATE_DEFAULT -> {
                binding.playIV.visibility = View.VISIBLE
                binding.pauseIV.visibility = View.INVISIBLE
            }
        }
    }

    private fun formatTime(milliseconds: Int): String {
        val seconds = milliseconds / 1000
        return String.format("%d:%02d", seconds / 60, seconds % 60)
    }
}