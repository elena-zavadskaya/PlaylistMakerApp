package com.practicum.playlistmakerapp.sharing.domain

import android.content.Context
import android.content.Intent
import android.net.Uri

class ExternalNavigator(private val context: Context) {

    // Поделиться приложением
    fun shareLink(shareAppLink: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareAppLink)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    // Условия использования
    fun openLink(termsLink: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(termsLink))
        context.startActivity(browserIntent)
    }

    // Служба поддержки
    fun openEmail(supportEmailData: EmailData) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.email))
            putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
            putExtra(Intent.EXTRA_TEXT, supportEmailData.body)
        }
        context.startActivity(emailIntent)
    }
}