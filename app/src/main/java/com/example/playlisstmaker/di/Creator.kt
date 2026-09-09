package com.example.playlisstmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlisstmaker.data.media.MediaPlayerManager
import com.example.playlisstmaker.data.media.ProgressTimer
import com.example.playlisstmaker.data.network.RetrofitNetworkClient
import com.example.playlisstmaker.data.repository.AudioPlayerRepositoryImpl
import com.example.playlisstmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlisstmaker.data.repository.SettingsRepositoryImpl
import com.example.playlisstmaker.data.repository.TracksRepositoryImpl
import com.example.playlisstmaker.domain.api.AudioPlayerInteractor

import com.example.playlisstmaker.domain.api.SearchHistoryInteractor
import com.example.playlisstmaker.domain.api.SearchHistoryRepository
import com.example.playlisstmaker.domain.api.SearchTracksInteractor
import com.example.playlisstmaker.domain.api.SettingsInteractor
import com.example.playlisstmaker.domain.api.SettingsRepository
import com.example.playlisstmaker.domain.api.TracksRepository
import com.example.playlisstmaker.domain.impl.AudioPlayerInteractorImpl
import com.example.playlisstmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlisstmaker.domain.impl.SearchTracksInteractorImpl
import com.example.playlisstmaker.domain.impl.SettingsInteractorImpl
import com.example.playlisstmaker.utils.Constants
import com.google.gson.Gson

object Creator {
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            Constants.SETTINGS_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSearchTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(getTracksRepository())
    }

    private fun getSearchHistoryRepository(
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPreferences, gson)
    }
    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(
            getSearchHistoryRepository(sharedPreferences, gson)
        )
    }

    private fun getSettingsRepository(
        sharedPreferences: SharedPreferences
    ): SettingsRepository {
        return SettingsRepositoryImpl(sharedPreferences)
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(
            getSettingsRepository(sharedPreferences)
        )
    }

    fun provideAudioPlayerInteractor(
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
        onError: () -> Unit,
        onStateChanged: (Int) -> Unit,
        onTimeUpdate: (String) -> Unit
    ): AudioPlayerInteractor {

        val progressTimer = ProgressTimer(onTimeUpdate)


        val mediaPlayerManager = MediaPlayerManager(
            onPrepared = onPrepared,
            onCompletion = onCompletion,
            onError = onError,
            onStateChanged = onStateChanged
        )


        val repository = AudioPlayerRepositoryImpl(mediaPlayerManager)


        return AudioPlayerInteractorImpl(repository,progressTimer)
    }
}