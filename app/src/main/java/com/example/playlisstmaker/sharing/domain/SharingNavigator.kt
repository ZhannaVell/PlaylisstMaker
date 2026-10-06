package com.example.playlisstmaker.sharing.domain

import com.example.playlisstmaker.sharing.domain.model.EmailData

interface SharingNavigator {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun openEmail(emailData: EmailData)
}