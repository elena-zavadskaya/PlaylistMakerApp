package com.practicum.playlistmakerapp.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.databinding.AudioPlayerBinding
import com.practicum.playlistmakerapp.DpToPx
import com.practicum.playlistmakerapp.creator.Creator
import com.practicum.playlistmakerapp.player.domain.models.Track

class AudioPlayerActivity : AppCompatActivity(){

    companion object {
        private const val DELAY = 1000L
    }

    private lateinit var viewModel: AudioPlayerViewModel
    private lateinit var binding: AudioPlayerBinding
    private lateinit var chosenTrack: Track

    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateUI(AudioPlayerViewModel.STATE_DEFAULT)

        viewModel = Creator.provideAudioPlayerViewModel()

        handler = Handler(Looper.getMainLooper())

        binding.backButton.setOnClickListener {
            finish()
        }

        val chosenTrackJson = intent.extras?.getString("KEY")
        chosenTrack = Gson().fromJson(chosenTrackJson, Track::class.java)

        bindTrackInfo()

        viewModel.prepareTrack(chosenTrack.previewUrl)

        binding.playIV.setOnClickListener {
            viewModel.playTrack()
            startTimer()
        }

        binding.pauseIV.setOnClickListener {
            viewModel.pauseTrack()
            stopTimer()
            handler?.removeCallbacksAndMessages(null)
        }

        viewModel.getPlayerStateLiveData().observe(this) { state ->
            updateUI(state)
        }

        viewModel.getTrackPositionLiveData().observe(this) { position ->
            binding.durationTV.text = formatTime(position)
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
            .format(chosenTrack.trackTimeMillis.toLong())
        binding.collectionNameTV.text = chosenTrack.collectionName
        binding.releaseDateTV.text = chosenTrack.releaseDate.substring(0, 4)
        binding.primaryGenreNameTV.text = chosenTrack.primaryGenreName
        binding.countryTV.text = chosenTrack.country
    }

    private fun startTimer() {
        handler?.post(timeCount())
    }

    private fun stopTimer() {
        handler?.removeCallbacksAndMessages(null)
    }

    private fun timeCount(): Runnable {
        return object : Runnable {
            override fun run() {
                viewModel.updateTrackPosition()
                handler?.postDelayed(this, DELAY)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseTrack()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        handler = null
    }

    private fun updateUI(state: Int) {
        Log.d("AudioPlayer", "Updating UI with state: $state")
        when(state) {
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
