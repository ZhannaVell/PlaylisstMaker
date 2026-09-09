package com.example.playlisstmaker.domain.api

interface SettingsRepository {
    fun getTheme() : Boolean
    fun saveTheme(isDark: Boolean)
}