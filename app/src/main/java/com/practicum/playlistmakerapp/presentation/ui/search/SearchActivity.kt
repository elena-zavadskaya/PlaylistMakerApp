package com.practicum.playlistmakerapp.presentation.ui.search

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
import androidx.core.view.isVisible
import com.google.gson.Gson
import com.practicum.playlistmakerapp.data.network.TrackResponse
import com.practicum.playlistmakerapp.data.network.ITunesApi
import com.practicum.playlistmakerapp.data.storage.SearchHistoryRepositoryImpl
import com.practicum.playlistmakerapp.databinding.ActivitySearchBinding
import com.practicum.playlistmakerapp.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SHARED_PREFERENCES_FOR_SEARCH_HISTORY = "shared_preference_for_search_history"
        const val SEARCH_HISTORY_KEY = "key_for_dark_theme"
        private const val KEY = "KEY"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchValue: String
    private val trackBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val searchService = retrofit.create(ITunesApi::class.java)
    private val tracks: MutableList<Track> = mutableListOf()
    private lateinit var trackAdapter: TrackAdapter
    private val searchRunnable = Runnable { search() }
    private val handler = Handler(Looper.getMainLooper())

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FOR_SEARCH_HISTORY, MODE_PRIVATE)
        val history = SearchHistoryRepositoryImpl(sharedPreferences, Gson())
        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty() && history.getTracks().isNotEmpty()) {
                showSearchHistory()
            } else {
                showEmptyPage()
            }
        }

        val searchHistoryAdapter = SearchHistoryAdapter {
            history.addTrack(it)
        }

        searchHistoryAdapter.historyTracks = history.getTracks()
        searchHistoryAdapter.notifyDataSetChanged()

        binding.searchHistoryRecyclerView.adapter = searchHistoryAdapter

        binding.clearHistoryButton.setOnClickListener {
            history.clearSearchHistory()
            searchHistoryAdapter.historyTracks = history.getTracks()
            searchHistoryAdapter.notifyDataSetChanged()
            showEmptyPage()
        }

        val listener =
            SharedPreferences.OnSharedPreferenceChangeListener { _: SharedPreferences, key: String? ->
                if (key == SEARCH_HISTORY_KEY) {
                    searchHistoryAdapter.historyTracks = history.getTracks()
                    searchHistoryAdapter.notifyDataSetChanged()
                }
            }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        trackAdapter = TrackAdapter(tracks) {
            history.addTrack(it)
        }

        binding.recyclerView.adapter = trackAdapter

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.clearIcon.setOnClickListener {
            tracks.clear()
            searchValue = binding.inputEditText.setText("").toString()
            if (history.getTracks().isEmpty()) {
                searchHistoryAdapter.notifyDataSetChanged()
                showEmptyPage()
            } else {
                showSearchHistory()
            }
        }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchValue = p0.toString()
                binding.clearIcon.isVisible = clearButtonVisibility(p0)
                if (binding.inputEditText.hasFocus() && p0?.isEmpty() == true) {
                    if (history.getTracks().isEmpty()) {
                        showEmptyPage()
                    } else {
                        searchHistoryAdapter.notifyDataSetChanged()
                        showSearchHistory()
                    }
                } else if (p0?.isEmpty() == false){
                    searchDebounce()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }

        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        binding.reloadButton.setOnClickListener {
            search()
        }

        binding.inputEditText.setOnClickListener {
            if (history.getTracks().isEmpty()) {
                showEmptyPage()
            } else {
                searchHistoryAdapter.notifyDataSetChanged()
                showSearchHistory()
            }
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchValue = binding.inputEditText.text.toString()
        outState.putString(KEY, searchValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchValue = savedInstanceState.getString(KEY).toString()
        binding.inputEditText.setText(searchValue)
    }

    private fun search() {
        if (searchValue.isNotEmpty()) {
            binding.searchHistory.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            binding.notFound.visibility = View.GONE
            binding.internetError.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            searchService
                .trackSearch(searchValue)
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        binding.progressBar.visibility = View.GONE
                        checkResponse(response)
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        binding.progressBar.visibility = View.GONE
                        showInternetErrorPage()
                    }
                })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun checkResponse(response:  Response<TrackResponse>) {
        if (response.code() == 200) {
            tracks.clear()
            if (response.body()?.results?.isNotEmpty() == true) {
                tracks.addAll(response.body()?.results!!)
                trackAdapter.notifyDataSetChanged()
                showTrackList()
            } else if (tracks.isEmpty()) {
                showNotFoundPage()
            }
        }
    }

    private fun showTrackList() {
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

    private fun showSearchHistory() {
        binding.recyclerView.isVisible = false
        binding.notFound.isVisible = false
        binding.internetError.isVisible = false
        binding.searchHistory.isVisible = true
    }
}
