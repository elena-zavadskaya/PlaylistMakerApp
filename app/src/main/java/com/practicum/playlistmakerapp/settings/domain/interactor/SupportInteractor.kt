package com.practicum.playlistmakerapp.settings.domain.interactor

interface SupportInteractor {
    fun getSupportEmail(): String
    fun getSupportSubject(): String
    fun getSupportMessage(): String
}