package com.practicum.playlistmakerapp.create.ui

import android.net.Uri
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
    fun setPlaylistId(id: Long) {
        _playlistId.value = id
    }

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

    override fun savePlaylist(name: String, description: String, coverImageUri: Uri?) {
        viewModelScope.launch {
            try {
                val playlistId = _playlistId.value
                    ?: throw IllegalArgumentException("ID плейлиста не найден")

                val playlist = createPlaylistInteractor.getPlaylistById(playlistId)
                    ?: throw IllegalArgumentException("Плейлист с id $playlistId не найден")

                val coverImagePath = coverImageUri?.let {
                    createPlaylistInteractor.saveImage(it).toString()
                } ?: playlist.coverImagePath

                val updatedPlaylist = playlist.copy(
                    name = name,
                    description = description,
                    coverImagePath = coverImagePath
                )

                createPlaylistInteractor.updatePlaylist(updatedPlaylist)

                _toastMessage.postValue("Плейлист \"$name\" обновлен")
                _playlistCreated.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                _toastMessage.postValue("Ошибка при обновлении плейлиста: ${e.message}")
                _playlistCreated.postValue(false)
            }
        }
    }
}
