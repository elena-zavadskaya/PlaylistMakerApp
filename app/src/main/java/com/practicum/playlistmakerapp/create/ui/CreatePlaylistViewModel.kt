package com.practicum.playlistmakerapp.create.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmakerapp.create.domain.interactor.CreatePlaylistInteractor
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val createPlaylistInteractor: CreatePlaylistInteractor
) : ViewModel() {

    private val _isCreateButtonEnabled = MutableLiveData(false)
    val isCreateButtonEnabled: LiveData<Boolean> get() = _isCreateButtonEnabled

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun onPlaylistNameChanged(name: String) {
        _isCreateButtonEnabled.value = name.isNotBlank()
    }

    fun createPlaylist(name: String, description: String, coverImagePath: String?) {
        viewModelScope.launch {
            try {
                createPlaylistInteractor.createPlaylist(
                    name = name,
                    description = description,
                    coverImagePath = coverImagePath ?: ""
                )
                _toastMessage.postValue("Плейлист \"$name\" создан")
            } catch (e: Exception) {
                e.printStackTrace()
                _toastMessage.postValue("Ошибка при создании плейлиста")
            }
        }
    }
}