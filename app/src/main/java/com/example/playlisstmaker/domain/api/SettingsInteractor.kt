package com.example.playlisstmaker.domain.api

interface SettingsInteractor {
    fun getTheme() : Boolean
    fun saveTheme(isDark: Boolean)
    fun toggleTheme()
}