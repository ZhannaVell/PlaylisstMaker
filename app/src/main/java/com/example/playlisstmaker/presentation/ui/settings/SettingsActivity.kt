package com.example.playlisstmaker.presentation.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.playlisstmaker.App
import com.example.playlisstmaker.R
import com.example.playlisstmaker.di.Creator
import com.example.playlisstmaker.domain.api.SettingsInteractor
import com.example.playlisstmaker.utils.Constants
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }



            val backButton = findViewById<MaterialToolbar>(R.id.tbSettings)
        val shareItem = findViewById<MaterialTextView>(R.id.tv_share)
        val supportItem = findViewById<MaterialTextView>(R.id.tv_support)
        val agreementItem = findViewById<MaterialTextView>(R.id.tv_agreement)


        settingsInteractor = Creator.provideSettingsInteractor()

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switch)
        themeSwitcher.isChecked = settingsInteractor.getTheme()

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            settingsInteractor.saveTheme(isChecked)
            (applicationContext as App).switchTheme(isChecked)
        }

        backButton.setNavigationOnClickListener {
            finish()
        }
        shareItem.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT,getString(R.string.share_message))
            }
            startActivity(Intent.createChooser(shareIntent,null))
        }
        supportItem.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
                putExtra(Intent.EXTRA_TEXT,getString(R.string.support_body))

            }
            startActivity(Intent.createChooser(emailIntent,null))
        }
        agreementItem.setOnClickListener {
            val termsUrl = getString(R.string.terms_url)
            val browserIntent = Intent(Intent.ACTION_VIEW, (termsUrl.toUri()))
            startActivity(browserIntent)
        }


    }
}