package com.practicum.playlistmakerapp.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmakerapp.databinding.ActivitySearchBinding
import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.player.ui.AudioPlayerActivity
import com.practicum.playlistmakerapp.search.SearchViewModel
import com.practicum.playlistmakerapp.search.TracksState

class SearchActivity : ComponentActivity() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val adapter = TrackAdapter(emptyList()) { track ->
        if (clickDebounce()) {
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra("poster", track.artworkUrl100)
            startActivity(intent)
            viewModel.addToSearchHistory(track)
        }
    }

    private val historyAdapter = TrackAdapter(emptyList()) {
        if (clickDebounce()) {
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra("poster", it.artworkUrl100)
            startActivity(intent)
            viewModel.addToSearchHistory(it)
        }
    }

    private lateinit var queryInput: EditText
    private lateinit var tracksList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var historyList: RecyclerView
    private lateinit var notFoundPage: LinearLayout
    private lateinit var internetErrorPage: LinearLayout
    private lateinit var clearButton: ImageView

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory())[SearchViewModel::class.java]

        queryInput = binding.inputEditText
        tracksList = binding.recyclerView
        progressBar = binding.progressBar
        historyList = binding.searchHistoryRecyclerView
        notFoundPage = binding.notFound
        internetErrorPage = binding.internetError
        clearButton = binding.clearIcon

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

        clearButton.setOnClickListener {
            queryInput.text.clear()
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
            historyAdapter.updateTracks(history)
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
        historyList.isVisible = false
        tracksList.isVisible = false
        progressBar.isVisible = true
        notFoundPage.isVisible = false
        internetErrorPage.isVisible = false
    }

    private fun showTrackList() {
        historyList.isVisible = false
        tracksList.isVisible = true
        progressBar.isVisible = false
        notFoundPage.isVisible = false
        internetErrorPage.isVisible = false
    }

    private fun showNotFoundPage() {
        historyList.isVisible = false
        tracksList.isVisible = false
        progressBar.isVisible = false
        notFoundPage.isVisible = true
        internetErrorPage.isVisible = false
    }

    private fun showInternetErrorPage() {
        historyList.isVisible = false
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
