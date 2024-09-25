package com.practicum.playlistmakerapp.settings.domain.impl

import com.practicum.playlistmakerapp.settings.domain.interactor.TermsInteractor
import com.practicum.playlistmakerapp.settings.domain.repository.TermsRepository

class TermsInteractorImpl(private val termsRepository: TermsRepository) : TermsInteractor {
    override fun getTermsOfUseLink(): String {
        return termsRepository.getTermsOfUseLink()
    }
}
