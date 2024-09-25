package com.practicum.playlistmakerapp.settings.domain.repository

interface SupportRepository {
    fun getSupportEmail(): String
    fun getSupportSubject(): String
    fun getSupportMessage(): String
}
