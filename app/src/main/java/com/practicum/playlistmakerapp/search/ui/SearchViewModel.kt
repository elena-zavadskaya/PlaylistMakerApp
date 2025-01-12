package com.practicum.playlistmakerapp.search.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.search.TracksState
import com.practicum.playlistmakerapp.search.domain.interactor.SearchHistoryInteractor
import com.practicum.playlistmakerapp.search.domain.api.SearchInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application,
    private val searchInteractor: SearchInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<TracksState>()
    private val historyLiveData = MutableLiveData<List<Track>>()
    private var latestSearchText: String? = null
    private var searchJob: Job? = null
    private var clickJob: Job? = null
    private var isClickAllowed = true

    fun observeState(): LiveData<TracksState> = stateLiveData
    fun observeHistory(): LiveData<List<Track>> = historyLiveData

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
        clickJob?.cancel()
    }

    fun addToSearchHistory(track: Track) {
        searchHistoryInteractor.addTrackToHistory(track)
    }

    fun showSearchHistory() {
        historyLiveData.postValue(searchHistoryInteractor.getSearchHistory())
    }

    fun clearSearchHistory() {
        searchHistoryInteractor.clearSearchHistory()
        historyLiveData.postValue(emptyList())
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText

        searchJob?.cancel()

        if (changedText.isNotEmpty()) {
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                performSearch(changedText)
            }
        }
    }

    private suspend fun performSearch(query: String) {
        renderState(TracksState.Loading)

        try {
            val tracks = searchInteractor.searchTracks(query)

            if (tracks.isNotEmpty()) {
                renderState(TracksState.Content(tracks))
            } else {
                renderState(TracksState.Empty)
            }
        } catch (e: Exception) {
            renderState(TracksState.Error)
        }
    }

    fun handleTrackClick(track: Track, onTrackClicked: (Track) -> Unit) {
        if (clickDebounce()) {
            addToSearchHistory(track)
            onTrackClicked(track)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            clickJob?.cancel()
            clickJob = viewModelScope.launch {
                delay(1000L)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    fun clearSearchResults() {
        renderState(TracksState.Empty)
        if (searchHistoryInteractor.getSearchHistory().isNotEmpty()) {
            historyLiveData.postValue(searchHistoryInteractor.getSearchHistory())
        } else {
            historyLiveData.postValue(emptyList())
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}