package com.example.playlisstmaker.settings.ui.activity.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope

import com.example.playlisstmaker.settings.domain.SettingsInteractor
import com.example.playlisstmaker.settings.domain.model.ThemeSettings
import com.example.playlisstmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val themeLiveData = MutableLiveData<Boolean>()
    fun observeTheme(): LiveData<Boolean> = themeLiveData

    init {
        viewModelScope.launch {
            settingsInteractor.themeFlow.collect { themeSettings ->
                themeLiveData.value = themeSettings.isDarkTheme
            }
        }
    }

    fun onThemeToggled(isDark: Boolean) {
        themeLiveData.value = isDark
        viewModelScope.launch {
            settingsInteractor.updateThemeSetting(ThemeSettings(isDarkTheme = isDark))
        }
    }

    fun shareApp() = sharingInteractor.shareApp()
    fun openSupport() = sharingInteractor.openSupport()
    fun openTerms() = sharingInteractor.openTerms()


}