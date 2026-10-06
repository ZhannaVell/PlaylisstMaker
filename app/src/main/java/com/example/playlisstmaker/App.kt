package com.example.playlisstmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

import com.example.playlisstmaker.di.dataModule
import com.example.playlisstmaker.di.interactorModule
import com.example.playlisstmaker.di.repositoryModule
import com.example.playlisstmaker.di.viewModelModule

import com.example.playlisstmaker.utils.ThemeCache
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        switchTheme(ThemeCache.isDarkTheme)

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}