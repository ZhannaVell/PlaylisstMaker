package com.example.playlisstmaker.creator

import android.content.Context
import com.example.playlisstmaker.R
import com.example.playlisstmaker.search.data.SearchHistoryDataStore
import com.example.playlisstmaker.search.data.network.RetrofitNetworkClient
import com.example.playlisstmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.example.playlisstmaker.settings.data.SettingsRepositoryImpl
import com.example.playlisstmaker.search.data.repository.TracksRepositoryImpl
import com.example.playlisstmaker.search.domain.SearchHistoryInteractor
import com.example.playlisstmaker.search.domain.SearchHistoryRepository
import com.example.playlisstmaker.search.domain.SearchTracksInteractor
import com.example.playlisstmaker.settings.domain.SettingsInteractor
import com.example.playlisstmaker.settings.domain.SettingsRepository
import com.example.playlisstmaker.search.domain.TracksRepository
import com.example.playlisstmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlisstmaker.search.domain.impl.SearchTracksInteractorImpl
import com.example.playlisstmaker.settings.data.SettingsDataStore
import com.example.playlisstmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlisstmaker.sharing.data.ExternalNavigator
import com.example.playlisstmaker.sharing.domain.SharingInteractor
import com.example.playlisstmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlisstmaker.sharing.domain.model.EmailData

object Creator {
    private lateinit var appContext: Context


    fun init(context: Context) {
        appContext = context.applicationContext

    }

    //Search

    private fun getTracksRepository(): TracksRepository =
        TracksRepositoryImpl(RetrofitNetworkClient())

    fun provideSearchTracksInteractor(): SearchTracksInteractor =
        SearchTracksInteractorImpl(getTracksRepository())

    private fun getSearchHistoryRepository(): SearchHistoryRepository =
        SearchHistoryRepositoryImpl(
            SearchHistoryDataStore(appContext)
        )

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor =
        SearchHistoryInteractorImpl(getSearchHistoryRepository())

    //Settings

    private fun getSettingsRepository(): SettingsRepository =
        SettingsRepositoryImpl(SettingsDataStore(appContext))

    fun provideSettingsInteractor(): SettingsInteractor =
        SettingsInteractorImpl(getSettingsRepository())

//Sharing


    fun provideSharingInteractor(): SharingInteractor =
        SharingInteractorImpl(
            navigator = ExternalNavigator(appContext),
            shareLink = appContext.getString(R.string.share_message),
            termsLink = appContext.getString(R.string.terms_url),
            supportEmailData = EmailData(
                email = appContext.getString(R.string.support_email),
                subject = appContext.getString(R.string.support_subject),
                body = appContext.getString(R.string.support_body),
            ),
        )
}
