package com.practicum.playlistmakerapp.create.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmakerapp.create.domain.interactor.CreatePlaylistInteractor
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel(
    protected val createPlaylistInteractor: CreatePlaylistInteractor
) : ViewModel() {

    private val _isCreateButtonEnabled = MutableLiveData(false)
    val isCreateButtonEnabled: LiveData<Boolean> get() = _isCreateButtonEnabled

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private val _playlistCreated = MutableLiveData<Boolean>()
    val playlistCreated: LiveData<Boolean> get() = _playlistCreated

    fun onPlaylistNameChanged(name: String) {
        _isCreateButtonEnabled.value = name.isNotBlank()
    }

     open fun createPlaylist(name: String, description: String, coverImageUri: Uri?) {
        viewModelScope.launch {
            try {
                val coverImagePath = coverImageUri?.let {
                    createPlaylistInteractor.saveImage(it).toString()
                } ?: ""

                createPlaylistInteractor.createPlaylist(name, description, coverImagePath)

                _toastMessage.postValue("Плейлист \"$name\" создан")
                _playlistCreated.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                _toastMessage.postValue("Ошибка при создании плейлиста")
                _playlistCreated.postValue(false)
            }
        }
    }
}