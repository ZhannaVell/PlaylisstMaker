package com.example.playlisstmaker.domain.impl

import com.example.playlisstmaker.domain.api.SettingsInteractor
import com.example.playlisstmaker.domain.api.SettingsRepository

class SettingsInteractorImpl (
    private val repository: SettingsRepository
) : SettingsInteractor {

    override fun getTheme(): Boolean {
        return repository.getTheme()
    }

    override fun saveTheme(isDark: Boolean) {
        repository.saveTheme(isDark)
    }

    override fun toggleTheme() {
        val current = repository.getTheme()
        repository.saveTheme(!current)
    }
}