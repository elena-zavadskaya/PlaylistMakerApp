package com.practicum.playlistmakerapp.create.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmakerapp.create.domain.interactor.CreatePlaylistInteractor
import com.practicum.playlistmakerapp.media.domain.model.Playlist
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    createPlaylistInteractor: CreatePlaylistInteractor
) : CreatePlaylistViewModel(createPlaylistInteractor) {

    private val _playlistId = MutableLiveData<Long>()
    val playlistId: LiveData<Long> get() = _playlistId

    fun getPlaylistById(id: Long): LiveData<Playlist?> {
        val playlistLiveData = MutableLiveData<Playlist?>()
        viewModelScope.launch {
            val playlistEntity = createPlaylistInteractor.getPlaylistById(id)
            playlistLiveData.postValue(
                playlistEntity?.let {
                    Playlist(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        coverImagePath = it.coverImagePath,
                        trackIds = it.trackIds,
                        trackCount = it.trackCount
                    )
                }
            )
        }
        return playlistLiveData
    }

}
