package com.example.playlisstmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlisstmaker.creator.Creator
import com.example.playlisstmaker.utils.ThemeCache


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.init(this)
        switchTheme(ThemeCache.isDarkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}