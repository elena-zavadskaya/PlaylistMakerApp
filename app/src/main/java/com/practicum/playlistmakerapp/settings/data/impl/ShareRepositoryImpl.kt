package com.practicum.playlistmakerapp.settings.data.impl

import android.content.Context
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.settings.domain.repository.ShareRepository

class ShareRepositoryImpl(private val context: Context) : ShareRepository {

    override fun getShareAppMessage(): String {
        return context.getString(R.string.share_app_message)
    }
}
