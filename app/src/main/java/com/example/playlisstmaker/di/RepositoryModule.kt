package com.example.playlisstmaker.di

import com.example.playlisstmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.example.playlisstmaker.search.data.repository.TracksRepositoryImpl
import com.example.playlisstmaker.search.domain.SearchHistoryRepository
import com.example.playlisstmaker.search.domain.TracksRepository
import com.example.playlisstmaker.settings.data.SettingsRepositoryImpl
import com.example.playlisstmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }
    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}