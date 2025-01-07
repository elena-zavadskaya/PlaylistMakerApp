package com.practicum.playlistmakerapp.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmakerapp.media.domain.interactor.FavoritesInteractor
import com.practicum.playlistmakerapp.player.domain.models.Track
import kotlinx.coroutines.launch

sealed class FavoritesState {
    object Empty : FavoritesState()
    data class Loaded(val tracks: List<Track>) : FavoritesState()
}

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val _state = MutableLiveData<FavoritesState>()
    val state: LiveData<FavoritesState> get() = _state

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            favoritesInteractor.getFavoriteTracks().collect { tracks ->
                if (tracks.isEmpty()) {
                    _state.postValue(FavoritesState.Empty)
                } else {
                    _state.postValue(FavoritesState.Loaded(tracks))
                }
            }
        }
    }
}