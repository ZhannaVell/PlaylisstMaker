package com.example.playlisstmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlisstmaker.di.Creator
import com.example.playlisstmaker.utils.Constants.DARK_THEME_KEY
import com.example.playlisstmaker.utils.Constants.SETTINGS_PREFERENCES


class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.init(this)
        val isDark = Creator.provideSettingsInteractor().getTheme()

        switchTheme(isDark)
    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}