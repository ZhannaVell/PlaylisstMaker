package com.example.playlisstmaker.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlisstmaker.App
import com.example.playlisstmaker.databinding.FragmentSettingsBinding
import com.example.playlisstmaker.settings.ui.activity.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()
    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupThemeSwitcher()
        setupSharingButtons()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        viewModel.observeTheme().observe(viewLifecycleOwner) { isDark ->
            binding.themeSwitch.isChecked = isDark
            (requireActivity().applicationContext as App).switchTheme(isDark)
        }
    }
}



