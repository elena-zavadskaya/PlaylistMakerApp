package com.practicum.playlistmakerapp.settings.data.impl

import android.content.Context
import com.practicum.playlistmakerapp.R

class TermsRepositoryImpl(private val context: Context) : TermsRepository {
    override fun getTermsOfUseLink(): String {
        return context.getString(R.string.terms_of_use_link)
    }
}
