package com.example.playlisstmaker.sharing.data

import android.content.Context
import android.content.Intent

import androidx.core.net.toUri
import com.example.playlisstmaker.sharing.domain.SharingNavigator
import com.example.playlisstmaker.sharing.domain.model.EmailData
import com.example.playlisstmaker.utils.Constants
import com.example.playlisstmaker.utils.Constants.MIME_TYPE_TEXT_PLAIN

class ExternalNavigator(private val context: Context) : SharingNavigator {

    override fun shareLink(link: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = MIME_TYPE_TEXT_PLAIN
            putExtra(Intent.EXTRA_TEXT, link)

        }
        val chooser = Intent.createChooser(intent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(chooser)

    }

    override fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, link.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)   // ← ЭТО
        }

        context.startActivity(intent)

    }

    override fun openEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Constants.MAILTO_PREFIX.toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.body)

        }
        val chooser = Intent.createChooser(intent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(chooser)
    }
}