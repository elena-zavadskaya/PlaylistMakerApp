package com.practicum.playlistmakerapp.settings

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmakerapp.App
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.creator.Creator
//import com.practicum.playlistmakerapp.search.SearchViewModel.Companion.SEARCH_REQUEST_TOKEN
import com.practicum.playlistmakerapp.search.TracksState
import com.practicum.playlistmakerapp.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmakerapp.settings.data.impl.ThemeRepositoryImpl
import com.practicum.playlistmakerapp.settings.domain.interactor.ShareInteractor
import com.practicum.playlistmakerapp.settings.domain.interactor.SupportInteractor
import com.practicum.playlistmakerapp.settings.domain.interactor.TermsInteractor
import com.practicum.playlistmakerapp.settings.domain.interactor.ThemeInteractor

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val SETTINGS_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

//    private val isDarkThemeLiveData = MutableLiveData<Boolean>()
//    fun observeState(): LiveData<ThemeState> = stateLiveData
//

    private val themeInteractor: ThemeInteractor = Creator.provideThemeInteractor(application)
    private val shareInteractor: ShareInteractor = Creator.provideShareInteractor(application)
    private val supportInteractor: SupportInteractor = Creator.provideSupportInteractor(application)
    private val termsInteractor: TermsInteractor = Creator.provideTermsInteractor(application)

    private val _isDarkTheme = MutableLiveData<Boolean>()

    private val handler = Handler(Looper.getMainLooper())

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SETTINGS_REQUEST_TOKEN)
    }

    init {
        _isDarkTheme.value = themeInteractor.isDarkThemeEnabled()
    }

    fun isDarkThemeEnabled(): Boolean {
        return themeInteractor.isDarkThemeEnabled()
    }

    fun switchTheme(isChecked: Boolean) {
        themeInteractor.switchTheme(isChecked)
        _isDarkTheme.value = isChecked
    }

    fun getShareAppMessage(): String {
        return shareInteractor.getShareAppMessage()
    }

    fun getSupportEmail(): String {
        return supportInteractor.getSupportEmail()
    }

    fun getSupportSubject(): String {
        return supportInteractor.getSupportSubject()
    }

    fun getSupportMessage(): String {
        return supportInteractor.getSupportMessage()
    }

    fun getTermsOfUseLink(): String {
        return termsInteractor.getTermsOfUseLink()
    }
}
