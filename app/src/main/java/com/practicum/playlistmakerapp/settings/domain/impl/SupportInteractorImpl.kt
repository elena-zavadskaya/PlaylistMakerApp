package com.practicum.playlistmakerapp.settings.domain.impl

import com.practicum.playlistmakerapp.settings.domain.interactor.SupportInteractor
import com.practicum.playlistmakerapp.settings.domain.repository.SupportRepository

class SupportInteractorImpl(private val supportRepository: SupportRepository) : SupportInteractor {
    override fun getSupportEmail(): String {
        return supportRepository.getSupportEmail()
    }

    override fun getSupportSubject(): String {
        return supportRepository.getSupportSubject()
    }

    override fun getSupportMessage(): String {
        return supportRepository.getSupportMessage()
    }
}