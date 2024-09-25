package com.practicum.playlistmakerapp.settings.data.impl

import android.content.Context
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.settings.domain.repository.TermsRepository

class TermsRepositoryImpl(private val context: Context) : TermsRepository {
    override fun getTermsOfUseLink(): String {
        return context.getString(R.string.terms_of_use_link)
    }
}
