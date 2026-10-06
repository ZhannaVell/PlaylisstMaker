package com.example.playlisstmaker.settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val context: Context) {


    val themeFlow: Flow<Boolean> = context.settingsDataStore.data
        .map { preferences ->
            preferences[DARK_THEME_KEY] ?: DEFAULT_DARK_THEME
        }


    suspend fun setTheme(isDark: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDark
        }
    }

    companion object {
        private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
            name = SETTINGS_DATASTORE_NAME
        )

        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        private const val SETTINGS_DATASTORE_NAME = "settings_preferences"
        private const val DEFAULT_DARK_THEME = false
    }
}