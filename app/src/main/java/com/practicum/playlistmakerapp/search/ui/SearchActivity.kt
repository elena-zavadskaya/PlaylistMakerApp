package com.practicum.playlistmakerapp.search.ui

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.ComponentActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.practicum.playlistmakerapp.player.data.network.TrackResponse
import com.practicum.playlistmakerapp.player.data.network.ITunesApi
import com.practicum.playlistmakerapp.player.data.storage.SearchHistoryRepositoryImpl
import com.practicum.playlistmakerapp.databinding.ActivitySearchBinding
import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.search.SearchViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : ComponentActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupUI()
    }

    private fun setupObservers() {
        searchViewModel.tracks.observe(this, Observer { tracks ->
            if (tracks.isNotEmpty()) {
                showTrackList(tracks)
            } else {
                showNotFoundPage()
            }
        })

        searchViewModel.searchError.observe(this, Observer { isError ->
            if (isError) {
                showInternetErrorPage()
            }
        })

        searchViewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.isVisible = isLoading
        })
    }

    private fun setupUI() {
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchViewModel.searchTracks(binding.inputEditText.text.toString())
                true
            }
            false
        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.text.clear()
            searchViewModel.clearSearchHistory()
            showEmptyPage()
        }

        binding.reloadButton.setOnClickListener {
            searchViewModel.searchTracks(binding.inputEditText.text.toString())
        }
    }

    private fun showTrackList(tracks: List<Track>) {
        binding.recyclerView.isVisible = true
        binding.notFound.isVisible = false
        binding.internetError.isVisible = false
        binding.searchHistory.isVisible = false
    }

    private fun showNotFoundPage() {
        binding.recyclerView.isVisible = false
        binding.notFound.isVisible = true
        binding.internetError.isVisible = false
        binding.searchHistory.isVisible = false
    }

    private fun showInternetErrorPage() {
        binding.recyclerView.isVisible = false
        binding.notFound.isVisible = false
        binding.internetError.isVisible = true
        binding.searchHistory.isVisible = false
    }

    private fun showEmptyPage() {
        binding.recyclerView.isVisible = false
        binding.notFound.isVisible = false
        binding.internetError.isVisible = false
        binding.searchHistory.isVisible = false
    }
}
