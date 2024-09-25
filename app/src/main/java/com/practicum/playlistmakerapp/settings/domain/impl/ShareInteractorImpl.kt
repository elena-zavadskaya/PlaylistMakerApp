package com.practicum.playlistmakerapp.settings.domain.impl

import com.practicum.playlistmakerapp.settings.domain.interactor.ShareInteractor
import com.practicum.playlistmakerapp.settings.domain.repository.ShareRepository

class ShareInteractorImpl(private val shareRepository: ShareRepository) : ShareInteractor {
    override fun getShareAppMessage(): String {
        return shareRepository.getShareAppMessage()
    }
}