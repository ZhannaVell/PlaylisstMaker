package com.example.playlisstmaker

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    companion object{
        private const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
    }
    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView
    private var searchText: String = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }


        val toolbar = findViewById<MaterialToolbar>(R.id.tbSearch)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        recyclerView = findViewById(R.id.rvTracks)
        adapter = TrackAdapter(MockData.trackList)
        recyclerView.adapter = adapter


        searchEditText = findViewById(R.id.searchEditText)
        clearButton = findViewById(R.id.clearButton)

        searchEditText.doOnTextChanged { text, start, before, count ->


                if (!text.isNullOrEmpty()) {
                    clearButton.visibility = View.VISIBLE
                    searchText = text.toString()
                } else {
                    clearButton.visibility = View.GONE
                    searchText = ""
                }

            }


        if (savedInstanceState != null) {
            val savedText = savedInstanceState.getString(SEARCH_TEXT_KEY, "")
            if (savedText.isNotEmpty()) {
                searchEditText.setText(savedText)
                searchEditText.setSelection(savedText.length)
            }
        }
        clearButton.setOnClickListener {
            searchEditText.setText("")
            hideKeyboard()

        }
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                true
            } else false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedText = savedInstanceState.getString(SEARCH_TEXT_KEY, "")
        if (savedText.isNotEmpty()) {
            searchEditText.setText(savedText)
            searchEditText.setSelection(savedText.length)
        }
    }


    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }

}