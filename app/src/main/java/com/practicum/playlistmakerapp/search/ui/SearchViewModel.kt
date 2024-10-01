package com.practicum.playlistmakerapp.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmakerapp.creator.Creator
import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.search.TracksState
import com.practicum.playlistmakerapp.search.domain.SearchHistoryInteractor
import com.practicum.playlistmakerapp.search.domain.api.SearchInteractor

class SearchViewModel(application: Application): AndroidViewModel(application) {

    private val searchInteractor = Creator.provideSearchInteractor()
    private val searchHistoryInteractor: SearchHistoryInteractor = Creator.provideSearchHistoryInteractor(application)
//    private val searchHistoryRepository = SearchHistoryRepositoryImpl(
//        getApplication<Application>().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//    )
    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private val historyLiveData = MutableLiveData<List<Track>>()
    fun observeHistory(): LiveData<List<Track>> = historyLiveData

    private var latestSearchText: String? = null

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun addToSearchHistory(track: Track) {
//        val currentHistory = historyLiveData.value.orEmpty().toMutableList()
//        if (!currentHistory.contains(track)) {
//            currentHistory.add(0, track)
//            historyLiveData.value = currentHistory
//        }
//        searchHistoryRepository.addTrack(track)
        searchHistoryInteractor.addTrackToHistory(track)
        loadSearchHistory()

    }

    private fun loadSearchHistory() {
        val history = searchHistoryInteractor.getSearchHistory()
        historyLiveData.postValue(history)
    }

    fun showSearchHistory() {
        historyLiveData.postValue(searchHistoryInteractor.getSearchHistory())
    }

    fun clearSearchHistory() {
//        searchHistoryRepository.clearHistory()
//        historyLiveData.postValue(emptyList())
        searchHistoryInteractor.clearSearchHistory()
        historyLiveData.postValue(emptyList())
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)

            searchInteractor.searchTracks(newSearchText, object : SearchInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?) {
                    val tracks = foundTracks ?: emptyList()
                    if (tracks.isNotEmpty()) {
                        renderState(TracksState.Content(tracks))
                    } else {
                        renderState(TracksState.Empty)
                    }
                }
            })
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
}