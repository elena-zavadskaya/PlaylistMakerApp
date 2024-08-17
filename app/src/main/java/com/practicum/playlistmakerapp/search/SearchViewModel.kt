package com.practicum.playlistmakerapp.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmakerapp.player.data.network.ITunesApi
import com.practicum.playlistmakerapp.player.data.network.TrackResponse
import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.search.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class SearchViewModel(
    private val searchHistoryRepository: SearchHistoryRepository,
    private val searchService: ITunesApi
) : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val _searchError = MutableLiveData<Boolean>()
    val searchError: LiveData<Boolean> get() = _searchError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun searchTracks(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            val response: Response<TrackResponse> = searchService.trackSearch(query).execute()
            _isLoading.postValue(false)
            if (response.isSuccessful) {
                _tracks.postValue(response.body()?.results ?: emptyList())
            } else {
                _searchError.postValue(true)
            }
        }
    }

    fun getSearchHistory(): List<Track> = searchHistoryRepository.getTracks()

    fun addTrackToHistory(track: Track) = searchHistoryRepository.addTrack(track)

    fun clearSearchHistory() = searchHistoryRepository.clearSearchHistory()
}