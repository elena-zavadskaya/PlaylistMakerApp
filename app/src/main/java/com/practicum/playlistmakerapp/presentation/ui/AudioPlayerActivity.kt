package com.practicum.playlistmakerapp.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.data.repository.AudioPlayerImpl
import com.practicum.playlistmakerapp.data.repository.TrackRepositoryImpl
import com.practicum.playlistmakerapp.databinding.AudioPlayerBinding
import com.practicum.playlistmakerapp.presentation.utils.DpToPx
import com.practicum.playlistmakerapp.domain.models.Track
import com.practicum.playlistmakerapp.domain.usecases.GetTrackUseCase
import com.practicum.playlistmakerapp.domain.usecases.PlayPauseTrackUseCase
import com.practicum.playlistmakerapp.domain.usecases.PrepareTrackUseCase
import com.practicum.playlistmakerapp.domain.usecases.UpdateTrackPositionUseCase
import com.practicum.playlistmakerapp.presentation.presenters.AudioPlayerPresenter

class AudioPlayerActivity : AppCompatActivity(), AudioPlayerPresenter.View {

    private lateinit var binding: AudioPlayerBinding
    private lateinit var presenter: AudioPlayerPresenter
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())

        val gson = Gson()
        val audioPlayer = AudioPlayerImpl()
        val trackRepository = TrackRepositoryImpl(gson, audioPlayer)

        presenter = AudioPlayerPresenter(
            GetTrackUseCase(trackRepository),
            PrepareTrackUseCase(trackRepository),
            PlayPauseTrackUseCase(trackRepository),
            UpdateTrackPositionUseCase(trackRepository),
            this
        )

        binding.backButton.setOnClickListener {
            finish()
        }

        val chosenTrackId = intent.extras?.getString("KEY")
        chosenTrackId?.let {
            presenter.onTrackSelected(it)
        }

        binding.playIV.setOnClickListener {
            handler?.post(timeCount())
            presenter.onPlayPauseClicked()
        }

        binding.pauseIV.setOnClickListener {
            handler?.removeCallbacksAndMessages(null)
            presenter.onPlayPauseClicked()
        }
    }

    override fun showTrackInfo(track: Track) {
        binding.trackTitleTV.text = track.trackName

        Glide.with(binding.albumIV)
            .load(track.getCoverArtwork())
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(DpToPx.dpToPx(2f, binding.albumIV)))
            .into(binding.albumIV)

        binding.trackAuthorTV.text = track.artistName
        binding.trackTimeMillisTV.text = formatTime(track.trackTimeMillis)
        binding.collectionNameTV.text = track.collectionName
        binding.releaseDateTV.text = track.releaseDate.substring(0, 4)
        binding.primaryGenreNameTV.text = track.primaryGenreName
        binding.countryTV.text = track.country
    }

    override fun updatePlayPauseButton(isPlaying: Boolean) {
        binding.playIV.visibility = if (isPlaying) View.INVISIBLE else View.VISIBLE
        binding.pauseIV.visibility = if (isPlaying) View.VISIBLE else View.INVISIBLE
    }

    override fun updateTrackPosition(position: String) {
        binding.durationTV.text = position
    }

    private fun startTimer() {
        handler?.post(timeCount())
    }

    private fun timeCount(): Runnable {
        return object : Runnable {
            override fun run() {
                presenter.updateTrackPosition()
                handler?.postDelayed(this, 1000L)
            }
        }
    }

    private fun formatTime(milliseconds: String): String {
        val seconds = milliseconds.toInt() / 1000
        return String.format("%d:%02d", seconds / 60, seconds % 60)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPlayPauseClicked()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }
}
