package com.practicum.playlistmakerapp.player.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.databinding.AudioPlayerBinding
import com.practicum.playlistmakerapp.DpToPx
import com.practicum.playlistmakerapp.create.data.db.PlaylistTrackEntity
import com.practicum.playlistmakerapp.create.ui.CreatePlaylistActivity
import com.practicum.playlistmakerapp.player.domain.models.Track
import org.koin.android.ext.android.inject

class AudioPlayerActivity : AppCompatActivity() {

    private val viewModel: AudioPlayerViewModel by inject()
    private val gson: Gson by inject()
    private lateinit var binding: AudioPlayerBinding
    private lateinit var chosenTrack: Track
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

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

        setupBottomSheet()

        binding.plusIV.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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

        viewModel.trackAddStatus.observe(this) { statusMessage ->
            Toast.makeText(this, statusMessage, Toast.LENGTH_SHORT).show()
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

    private fun setupBottomSheet() {
        val bottomSheetContainer = findViewById<LinearLayout>(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        viewModel.loadPlaylists()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        val newPlaylistButton = bottomSheetContainer.findViewById<View>(R.id.newPlaylistButton)
        newPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            navigateToCreatePlaylistScreen()
        }

        viewModel.playlists.observe(this) { playlists ->
            val recyclerView = bottomSheetContainer.findViewById<RecyclerView>(R.id.playlistsRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = BottomSheetAdapter(playlists) { selectedPlaylist ->
                val trackEntity = PlaylistTrackEntity(
                    id = chosenTrack.trackId.toString(),
                    coverUrl = chosenTrack.getCoverArtwork(),
                    title = chosenTrack.trackName,
                    artist = chosenTrack.artistName,
                    album = chosenTrack.collectionName,
                    releaseYear = chosenTrack.releaseDate.substring(0, 4).toInt(),
                    genre = chosenTrack.primaryGenreName,
                    country = chosenTrack.country,
                    duration = formatTime(chosenTrack.trackTimeMillis.toInt()),
                    fileUrl = chosenTrack.previewUrl
                )
                viewModel.addTrackToPlaylist(trackEntity, selectedPlaylist.id)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }


    private fun navigateToCreatePlaylistScreen() {
        val intent = Intent(this, CreatePlaylistActivity::class.java)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseTrack()
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.playerState.value == AudioPlayerViewModel.STATE_DEFAULT ||
            viewModel.playerState.value == AudioPlayerViewModel.STATE_PAUSED
        ) {
            viewModel.prepareTrack(chosenTrack)
        }
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