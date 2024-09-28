package com.practicum.playlistmakerapp.settings.data.impl

import android.content.Context
import com.practicum.playlistmakerapp.R

class SupportRepositoryImpl(private val context: Context) : SupportRepository {
    override fun getSupportEmail(): String {
        return context.getString(R.string.support_email)
    }

    override fun getSupportSubject(): String {
        return context.getString(R.string.write_to_support_subject)
    }

    override fun getSupportMessage(): String {
        return context.getString(R.string.write_to_support_message)
    }
}
