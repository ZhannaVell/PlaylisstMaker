package com.example.playlisstmaker.settings.domain.impl

import com.example.playlisstmaker.settings.domain.SettingsInteractor
import com.example.playlisstmaker.settings.domain.SettingsRepository
import com.example.playlisstmaker.settings.domain.model.ThemeSettings
import kotlinx.coroutines.flow.Flow

class SettingsInteractorImpl(
    private val repository: SettingsRepository
) : SettingsInteractor {

    override val themeFlow: Flow<ThemeSettings> = repository.themeFlow

    override suspend fun updateThemeSetting(settings: ThemeSettings) =
        repository.updateThemeSetting(settings)
}