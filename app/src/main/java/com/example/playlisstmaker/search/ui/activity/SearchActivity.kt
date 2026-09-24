package com.example.playlisstmaker.search.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider

import com.example.playlisstmaker.R

import com.example.playlisstmaker.databinding.ActivitySearchBinding
import com.example.playlisstmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlisstmaker.search.ui.adapter.TrackAdapter

import com.example.playlisstmaker.search.domain.model.Track
import com.example.playlisstmaker.search.ui.model.TrackParcelable
import com.example.playlisstmaker.search.ui.state.SearchState
import com.example.playlisstmaker.search.ui.view_model.SearchViewModel
import com.example.playlisstmaker.utils.Constants


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SearchViewModel.getFactory())
            .get(SearchViewModel::class.java)

        setupEdgeToEdge()
        setupToolbar()
        setupRecyclerView()
        setupHistoryRecyclerView()
        setupListeners()
        observeViewModel()

        binding.searchEditText.setText(viewModel.getQuery())
        binding.searchEditText.setSelection(binding.searchEditText.text?.length ?: 0)
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }
    }

    private fun setupToolbar() {
        binding.tbSearch.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = TrackAdapter { track ->
            if (clickDebounce()) {
                viewModel.onTrackClicked(track)
                openAudioPlayer(track)
            }
        }
        binding.rvTracks.adapter = adapter
    }

    private fun setupHistoryRecyclerView() {
        historyAdapter = TrackAdapter { track ->
            if (clickDebounce()) {
                viewModel.onHistoryTrackClicked(track)
                openAudioPlayer(track)
            }
        }
        binding.historyRecyclerView.adapter = historyAdapter

        binding.clearHistoryButton.setOnClickListener {
            viewModel.onClearHistoryClicked()
        }
    }

    private fun setupListeners() {
        binding.retryButton.setOnClickListener {
            viewModel.onRetryClicked()
        }

        // Ввод текста
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            val query = text?.toString() ?: ""
            binding.clearButton.isVisible = query.isNotEmpty()
            viewModel.onQueryChanged(query)
        }

        // Фокус
        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onFocusChanged(hasFocus)
        }

        // Крестик
        binding.clearButton.setOnClickListener {
            binding.searchEditText.setText("")
            hideKeyboard()
            viewModel.onClearQueryClicked()
        }

        // Enter
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onRetryClicked()  // перезапуск поиска
                true
            } else false
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, Constants.CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun openAudioPlayer(track: Track) {
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra(Constants.TRACK_EXTRA, TrackParcelable.from(track))
        startActivity(intent)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        binding.searchEditText.windowToken?.let {
            imm.hideSoftInputFromWindow(it, 0)
        }
    }

    private fun observeViewModel() {
        viewModel.observeState().observe(this) { state ->
            render(state)
        }
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Empty -> showEmpty()
            is SearchState.Error -> showError()
            is SearchState.History -> showHistory(state.tracks)
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.rvTracks.isVisible = false
        binding.placeholderContainer.isVisible = false
        hideHistory()
    }

    private fun showContent(tracks: List<Track>) {
        binding.progressBar.isVisible = false
        binding.placeholderContainer.isVisible = false
        binding.rvTracks.isVisible = true
        adapter.submitList(tracks)
        hideHistory()
    }

    private fun showEmpty() {
        binding.progressBar.isVisible = false
        binding.rvTracks.isVisible = false
        binding.placeholderContainer.isVisible = true

        binding.placeholderImage.setImageResource(R.drawable.ic_error_empty_120)
        binding.placeholderTitle.text = getString(R.string.empty_result)
        binding.errorSubtitle.isVisible = false
        binding.retryButton.isVisible = false

        hideHistory()
    }

    private fun showError() {
        binding.progressBar.isVisible = false
        binding.rvTracks.isVisible = false
        binding.placeholderContainer.isVisible = true

        binding.placeholderImage.setImageResource(R.drawable.ic_error_network_120)
        binding.placeholderTitle.text = getString(R.string.error_network_title)
        binding.errorSubtitle.text = getString(R.string.error_network_subtitle)
        binding.errorSubtitle.isVisible = true
        binding.retryButton.isVisible = true

        hideHistory()
    }

    private fun showHistory(tracks: List<Track>) {
        binding.llCacheContainer.isVisible = true
        binding.historyTitle.isVisible = true
        binding.historyRecyclerView.isVisible = true
        binding.clearHistoryButton.isVisible = true

        binding.progressBar.isVisible = false
        binding.rvTracks.isVisible = false
        binding.placeholderContainer.isVisible = false

        historyAdapter.submitList(tracks)
    }

    private fun hideHistory() {
        binding.llCacheContainer.isVisible = false
        binding.historyTitle.isVisible = false
        binding.historyRecyclerView.isVisible = false
        binding.clearHistoryButton.isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}