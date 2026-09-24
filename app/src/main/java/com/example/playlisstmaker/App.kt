package com.example.playlisstmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlisstmaker.creator.Creator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.init(this)
        val themeSettings = runBlocking {
            Creator.provideSettingsInteractor().themeFlow.first()
        }

        switchTheme(themeSettings.isDarkTheme)
    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}