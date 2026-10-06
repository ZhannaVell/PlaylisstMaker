package com.example.playlisstmaker.di

import com.example.playlisstmaker.R
import com.example.playlisstmaker.search.domain.SearchHistoryInteractor
import com.example.playlisstmaker.search.domain.SearchTracksInteractor
import com.example.playlisstmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlisstmaker.search.domain.impl.SearchTracksInteractorImpl
import com.example.playlisstmaker.settings.domain.SettingsInteractor
import com.example.playlisstmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlisstmaker.sharing.domain.SharingInteractor
import com.example.playlisstmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlisstmaker.sharing.domain.model.EmailData
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val interactorModule = module {
    factory<SearchTracksInteractor> {
        SearchTracksInteractorImpl(get())
    }
    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
    factory<SharingInteractor> {
        SharingInteractorImpl(
            navigator = get(),
            shareLink = androidContext().getString(R.string.share_message),
            termsLink = androidContext().getString(R.string.terms_url),
            supportEmailData = EmailData(
                email = androidContext().getString(R.string.support_email),
                subject = androidContext().getString(R.string.support_subject),
                body = androidContext().getString(R.string.support_body)
            )
        )
    }
}