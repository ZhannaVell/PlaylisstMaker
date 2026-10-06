package com.example.playlisstmaker.settings.domain

import com.example.playlisstmaker.settings.domain.model.ThemeSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeFlow: Flow<ThemeSettings>
    suspend fun updateThemeSetting(settings: ThemeSettings)
}