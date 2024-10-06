package com.practicum.playlistmakerapp.sharing.domain

import android.app.Application
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.sharing.data.EmailData
import com.practicum.playlistmakerapp.sharing.data.ExternalNavigator

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val application: Application
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return application.getString(R.string.share_app_message)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = application.getString(R.string.support_email),
            subject = application.getString(R.string.write_to_support_subject),
            body = application.getString(R.string.write_to_support_message)
        )
    }

    private fun getTermsLink(): String {
        return application.getString(R.string.terms_of_use_link)
    }
}