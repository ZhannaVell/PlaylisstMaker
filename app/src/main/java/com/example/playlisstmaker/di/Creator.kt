package com.example.playlisstmaker.di

import android.content.SharedPreferences
import com.example.playlisstmaker.data.media.MediaPlayerManager
import com.example.playlisstmaker.data.media.ProgressTimer
import com.example.playlisstmaker.data.network.RetrofitNetworkClient
import com.example.playlisstmaker.data.repository.AudioPlayerRepositoryImpl
import com.example.playlisstmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlisstmaker.data.repository.TracksRepositoryImpl
import com.example.playlisstmaker.domain.api.AudioPlayerInteractor
import com.example.playlisstmaker.domain.api.AudioPlayerRepository
import com.example.playlisstmaker.domain.api.SearchHistoryInteractor
import com.example.playlisstmaker.domain.api.SearchHistoryRepository
import com.example.playlisstmaker.domain.api.SearchTracksInteractor
import com.example.playlisstmaker.domain.api.TracksRepository
import com.example.playlisstmaker.domain.impl.AudioPlayerInteractorImpl
import com.example.playlisstmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlisstmaker.domain.impl.SearchTracksInteractorImpl
import com.google.gson.Gson

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSearchTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(getTracksRepository())
    }
    fun provideProgressTimer(
        onTimeUpdate: (String) -> Unit
    ): ProgressTimer {
        return ProgressTimer(onTimeUpdate)
    }
    fun provideMediaPlayerManager(
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
        onError: () -> Unit,
        onStateChanged: (Int) -> Unit
    ): MediaPlayerManager {
        return MediaPlayerManager(
            onPrepared = onPrepared,
            onCompletion = onCompletion,
            onError = onError,
            onStateChanged = onStateChanged
        )
    }

    private fun getSearchHistoryRepository(
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPreferences, gson)
    }
    fun provideSearchHistoryInteractor(
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(
            getSearchHistoryRepository(sharedPreferences, gson)
        )
    }
    private fun getAudioPlayerRepository(
        mediaPlayerManager: MediaPlayerManager,
        progressTimer: ProgressTimer
    ): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(mediaPlayerManager, progressTimer)
    }
    fun provideAudioPlayerInteractor(
        mediaPlayerManager: MediaPlayerManager,
        progressTimer: ProgressTimer
    ): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(
            getAudioPlayerRepository(mediaPlayerManager, progressTimer)
        )
    }
}