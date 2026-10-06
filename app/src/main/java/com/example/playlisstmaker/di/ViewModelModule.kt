package com.example.playlisstmaker.di

import com.example.playlisstmaker.player.ui.view_model.AudioPlayerViewModel
import com.example.playlisstmaker.search.ui.view_model.SearchViewModel
import com.example.playlisstmaker.settings.ui.activity.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(
            searchTracksInteractor = get(),
            searchHistoryInteractor = get(),
            resourceProvider = get()
        )
    }
    viewModel {
        SettingsViewModel(
            settingsInteractor = get(),
            sharingInteractor = get()
        )
    }
    viewModel { (url: String) ->
        AudioPlayerViewModel(
            url = url
        )
    }
}


