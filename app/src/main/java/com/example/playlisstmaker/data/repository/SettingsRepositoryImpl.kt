package com.example.playlisstmaker.data.repository

import android.content.SharedPreferences
import com.example.playlisstmaker.domain.api.SettingsRepository
import com.example.playlisstmaker.utils.Constants

class SettingsRepositoryImpl (
    private val sharedPreferences: SharedPreferences
    ): SettingsRepository {

    override fun getTheme(): Boolean {
        return sharedPreferences.getBoolean(Constants.DARK_THEME_KEY, false)
    }

    override fun saveTheme(isDark: Boolean) {
        sharedPreferences.edit()
            .putBoolean(Constants.DARK_THEME_KEY, isDark)
            .apply()
    }
}

