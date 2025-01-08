package com.practicum.playlistmakerapp.create.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmakerapp.create.domain.interactor.CreatePlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

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

    fun createPlaylist(name: String, description: String, imageUri: Uri?, contentResolver: (Uri) -> InputStream?) {
        if (imageUri == null) {
            _toastMessage.value = "Выберите обложку для плейлиста"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val copiedImagePath = copyImageToInternalStorage(imageUri, contentResolver)
            if (copiedImagePath != null) {
                createPlaylistInteractor.createPlaylist(name, description, copiedImagePath)
                _toastMessage.postValue("Плейлист \"$name\" создан")
            } else {
                _toastMessage.postValue("Не удалось сохранить обложку")
            }
        }
    }

    private suspend fun copyImageToInternalStorage(uri: Uri, contentResolver: (Uri) -> InputStream?): String? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = contentResolver(uri) ?: return@withContext null
                val file = File("playlist_${System.currentTimeMillis()}.jpg")
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                outputStream.close()
                inputStream.close()
                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}