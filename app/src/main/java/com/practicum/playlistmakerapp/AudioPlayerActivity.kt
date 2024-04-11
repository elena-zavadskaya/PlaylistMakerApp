package com.practicum.playlistmakerapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmakerapp.databinding.AudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DELAY = 1000L
    }

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private lateinit var play: ImageView
    private lateinit var pause: ImageView

    private lateinit var binding: AudioPlayerBinding
    private lateinit var chosenTrack: Track

    private var mainThreadHandler: Handler? = null
    private var duration: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mainThreadHandler = Handler(Looper.getMainLooper())

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
        play = findViewById(R.id.playIV)
        pause = findViewById(R.id.pauseIV)
        duration = findViewById(R.id.durationTV)


        trackName.text = chosenTrack.trackName

        Glide.with(trackImage)
            .load(chosenTrack.getCoverArtwork())
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

        preparePlayer(chosenTrack.previewUrl)

        play.setOnClickListener {
            startTimer()
            playbackControl()
        }

        pause.setOnClickListener {
            mainThreadHandler?.removeCallbacksAndMessages(null)
            playbackControl()
        }
    }

    private fun startTimer() {
        mainThreadHandler?.post(
            timeCount()
        )
    }

    private fun timeCount(): Runnable {
        return object : Runnable {
            override fun run() {
                val seconds = mediaPlayer.currentPosition / DELAY
                duration?.text = String.format("%d:%02d", seconds / 60, seconds % 60)
                mainThreadHandler?.postDelayed(this, DELAY).toString()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            play.visibility = View.VISIBLE
            pause.visibility = View.INVISIBLE
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        play.visibility = View.INVISIBLE
        pause.visibility = View.VISIBLE

    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        play.visibility = View.VISIBLE
        pause.visibility = View.INVISIBLE
    }
}