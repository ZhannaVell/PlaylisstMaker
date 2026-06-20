package com.example.playlisstmaker
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        val backButton = findViewById<MaterialToolbar>(R.id.tbSettings)
        val shareItem = findViewById<MaterialTextView>(R.id.tv_share)
        val supportItem = findViewById<MaterialTextView>(R.id.tv_support)
        val agreementItem = findViewById<MaterialTextView>(R.id.tv_agreement)

        backButton.setNavigationOnClickListener {
            finish()
        }
    }
}