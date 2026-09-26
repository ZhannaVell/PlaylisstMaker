package com.example.playlisstmaker.settings.data


import com.example.playlisstmaker.settings.domain.SettingsRepository
import com.example.playlisstmaker.settings.domain.model.ThemeSettings
import com.example.playlisstmaker.utils.ThemeCache

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: SettingsDataStore
) : SettingsRepository {

    override val themeFlow: Flow<ThemeSettings> = dataStore.themeFlow
        .map { isDark ->
            ThemeCache.isDarkTheme = isDark
            ThemeSettings(isDarkTheme = isDark)
        }


    override suspend fun updateThemeSetting(settings: ThemeSettings) {
        ThemeCache.isDarkTheme = settings.isDarkTheme
        dataStore.setTheme(settings.isDarkTheme)
    }
}