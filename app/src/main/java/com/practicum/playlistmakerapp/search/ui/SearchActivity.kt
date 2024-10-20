package com.practicum.playlistmakerapp.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.databinding.ActivitySearchBinding
import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.player.ui.AudioPlayerActivity
import com.practicum.playlistmakerapp.search.TracksState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val onTrackClick: (Track) -> Unit = {track ->
        if (isClickAllowed) {
            clickDebounce()
            viewModel.addToSearchHistory(track)
            val json = Gson().toJson(track)
            Intent(
                this,
                AudioPlayerActivity::class.java
            ).apply {
                putExtra("KEY", json)
                startActivity((this))
            }
        }
    }

    private val adapter = TrackAdapter(emptyList()) { track ->
        onTrackClick(track)
    }

    private val historyAdapter = TrackAdapter(emptyList()) { track ->
        onTrackClick(track)
    }

    private lateinit var queryInput: EditText
    private lateinit var tracksList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var historyList: RecyclerView
    private lateinit var searchHistory: ScrollView
    private lateinit var notFoundPage: LinearLayout
    private lateinit var internetErrorPage: LinearLayout
    private lateinit var toolbar: Toolbar
    private lateinit var clearButton: ImageView
    private lateinit var clearHistoryButton: Button

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observeState().observe(this) { render(it) }
        viewModel.observeHistory().observe(this) { showSearchHistory(it) }

        queryInput = binding.inputEditText
        tracksList = binding.recyclerView
        progressBar = binding.progressBar
        historyList = binding.searchHistoryRecyclerView
        searchHistory = binding.searchHistory
        notFoundPage = binding.notFound
        internetErrorPage = binding.internetError
        toolbar = findViewById(R.id.backbutton_toolbar)
        clearButton = binding.clearIcon
        clearHistoryButton = binding.clearHistoryButton

        tracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksList.adapter = adapter

        historyList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        historyList.adapter = historyAdapter

        queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && queryInput.text.isEmpty()) {
                viewModel.showSearchHistory()
            }
        }

        queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
                clearButton.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        toolbar.setNavigationOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            queryInput.text.clear()
        }

        clearHistoryButton.setOnClickListener {
            viewModel.clearSearchHistory()
        }

        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.observeHistory().observe(this) {
            showSearchHistory(it)
        }
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Content -> {
                adapter.updateTracks(state.tracks)
                showTrackList()
            }
            is TracksState.Empty -> showNotFoundPage()
            is TracksState.Error -> showInternetErrorPage()
            is TracksState.Loading -> showLoadingPage()
        }
    }

    private fun showSearchHistory(history: List<Track>) {
        if (history.isNotEmpty()) {
            historyList.isVisible = true
            binding.searchHistory.isVisible = true
            historyAdapter.updateTracks(history)
            tracksList.isVisible = false
            progressBar.isVisible = false
            notFoundPage.isVisible = false
        } else {
            historyList.isVisible = false
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun showLoadingPage() {
        searchHistory.isVisible = false
        tracksList.isVisible = false
        progressBar.isVisible = true
        notFoundPage.isVisible = false
        internetErrorPage.isVisible = false
    }

    private fun showTrackList() {
        searchHistory.isVisible = false
        tracksList.isVisible = true
        progressBar.isVisible = false
        notFoundPage.isVisible = false
        internetErrorPage.isVisible = false
    }

    private fun showNotFoundPage() {
        searchHistory.isVisible = false
        tracksList.isVisible = false
        progressBar.isVisible = false
        notFoundPage.isVisible = true
        internetErrorPage.isVisible = false
    }

    private fun showInternetErrorPage() {
        searchHistory.isVisible = false
        tracksList.isVisible = false
        progressBar.isVisible = false
        notFoundPage.isVisible = false
        internetErrorPage.isVisible = true
    }

    override fun onDestroy() {
        super.onDestroy()
        queryInput.removeTextChangedListener(null)
    }
}
