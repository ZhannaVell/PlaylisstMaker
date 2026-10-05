package com.example.playlisstmaker.settings.ui.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

import com.example.playlisstmaker.App


import com.example.playlisstmaker.databinding.ActivitySettingsBinding

import com.example.playlisstmaker.settings.ui.activity.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel



class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setupEdgeToEdge()
        setupToolbar()
        setupThemeSwitcher()
        setupSharingButtons()
        observeViewModel()
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }
    }

    private fun setupToolbar() {
        binding.tbSettings.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupThemeSwitcher() {
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onThemeToggled(isChecked)
        }
    }

    private fun setupSharingButtons() {
        binding.tvShare.setOnClickListener {
            viewModel.shareApp()
        }
        binding.tvSupport.setOnClickListener {
            viewModel.openSupport()
        }
        binding.tvAgreement.setOnClickListener {
            viewModel.openTerms()
        }
    }

    private fun observeViewModel() {
        viewModel.observeTheme().observe(this) { isDark ->
            binding.themeSwitch.isChecked = isDark
            (applicationContext as App).switchTheme(isDark)
        }
    }
}