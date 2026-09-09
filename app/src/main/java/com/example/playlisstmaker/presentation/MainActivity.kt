package com.example.playlisstmaker.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.playlisstmaker.R
import com.example.playlisstmaker.presentation.ui.search.SearchActivity
import com.example.playlisstmaker.presentation.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

        val buttonSearch = findViewById<Button>(R.id.btn_search)
        val buttonMedia = findViewById<Button>(R.id.btn_media)
        val buttonSettings = findViewById<Button>(R.id.btn_settings)

        val searchClickListener = object : View.OnClickListener{
            override fun onClick(v: View?) {
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)

                //Toast.makeText(this@MainActivity, "Нажата кнопка Поиск", Toast.LENGTH_SHORT).show()
            }

        }
        buttonSearch.setOnClickListener(searchClickListener)



        buttonMedia.setOnClickListener{
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)

            //Toast.makeText(this, "Нажата кнопка Медиатека", Toast.LENGTH_SHORT).show()

        }
        buttonSettings.setOnClickListener{
            val settingIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingIntent)
            //Toast.makeText(this, "Нажата кнопка Настройки", Toast.LENGTH_SHORT).show()
        }

    }
}