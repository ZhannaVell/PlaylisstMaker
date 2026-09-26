package com.example.playlisstmaker.sharing.domain.impl


import com.example.playlisstmaker.sharing.domain.SharingInteractor
import com.example.playlisstmaker.sharing.domain.SharingNavigator
import com.example.playlisstmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val navigator: SharingNavigator,
    private val shareLink: String,
    private val termsLink: String,
    private val supportEmailData: EmailData,
) : SharingInteractor {

    override fun shareApp() {
        navigator.shareLink(shareLink)
    }

    override fun openTerms() {
        navigator.openLink(termsLink)
    }

    override fun openSupport() {
        navigator.openEmail(supportEmailData)
    }
}