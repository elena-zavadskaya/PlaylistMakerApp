package com.practicum.playlistmakerapp.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
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

    private val adapter = TrackAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra("poster", it.artworkUrl100)
            startActivity(intent)
        }
    }

    private lateinit var queryInput: EditText
    private lateinit var tracksList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textWatcher: TextWatcher
    private lateinit var searchHistory: ScrollView
    private lateinit var notFoundPage: LinearLayout
    private lateinit var internetErrorPage: LinearLayout

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
        searchHistory = binding.searchHistory
        notFoundPage = binding.notFound
        internetErrorPage = binding.internetError

        tracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksList.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { queryInput.addTextChangedListener(it) }

        viewModel.observeState().observe(this) {
            render(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { queryInput.removeTextChangedListener(it) }
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Content -> showTrackList()
            is TracksState.Empty -> showNotFoundPage()
            is TracksState.Error -> showInternetErrorPage()
            is TracksState.Loading -> showLoadingPage()
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

    private fun showEmptyPage() {
        searchHistory.isVisible = false
        tracksList.isVisible = false
        progressBar.isVisible = false
        notFoundPage.isVisible = false
        internetErrorPage.isVisible = false
    }

//    private fun setupObservers() {
//        viewModel.tracks.observe(this, Observer { tracks ->
//            if (tracks.isNotEmpty()) {
//                showTrackList(tracks)
//            } else {
//                showNotFoundPage()
//            }
//        })
//
//        viewModel.searchError.observe(this, Observer { isError ->
//            if (isError) {
//                showInternetErrorPage()
//            }
//        })
//
//        viewModel.isLoading.observe(this, Observer { isLoading ->
//            binding.progressBar.isVisible = isLoading
//        })
//    }
//
//    private fun setupUI() {
//        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                viewModel.searchTracks(binding.inputEditText.text.toString())
//                true
//            }
//            false
//        }
//
//        binding.clearIcon.setOnClickListener {
//            binding.inputEditText.text.clear()
//            viewModel.clearSearchHistory()
//            showEmptyPage()
//        }
//
//        binding.reloadButton.setOnClickListener {
//            viewModel.searchTracks(binding.inputEditText.text.toString())
//        }
//    }
}
