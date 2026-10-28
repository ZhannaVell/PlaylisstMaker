package com.example.playlisstmaker.search.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlisstmaker.R
import com.example.playlisstmaker.databinding.FragmentSearchBinding
import com.example.playlisstmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlisstmaker.search.domain.model.Track
import com.example.playlisstmaker.search.ui.adapter.TrackAdapter
import com.example.playlisstmaker.search.ui.model.TrackParcelable
import com.example.playlisstmaker.search.ui.state.SearchState
import com.example.playlisstmaker.search.ui.view_model.SearchViewModel
import com.example.playlisstmaker.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModel()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupHistoryRecyclerView()
        setupListeners()
        observeViewModel()

        binding.searchEditText.setText(viewModel.getQuery())
        binding.searchEditText.setSelection(binding.searchEditText.text?.length ?: 0)
        binding.placeholderContainer.isVisible = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
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
        findNavController().navigate(
            R.id.action_searchFragment_to_audioPlayerFragment,
            AudioPlayerFragment.createArgs(TrackParcelable.from(track))
        )
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.searchEditText.windowToken?.let {
            imm.hideSoftInputFromWindow(it, 0)
        }
    }

    private fun observeViewModel() {
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Idle -> showIdle()
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Empty -> showEmpty(state)
            is SearchState.Error -> showError(state)
            is SearchState.History -> showHistory(state.tracks)
        }
    }

    private fun showIdle() {
        binding.progressBar.isVisible = false
        binding.rvTracks.isVisible = false
        binding.placeholderContainer.isVisible = false
        hideHistory()
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

    private fun showEmpty(state: SearchState.Empty) {
        binding.progressBar.isVisible = false
        binding.rvTracks.isVisible = false
        binding.placeholderContainer.isVisible = true

        binding.placeholderImage.setImageResource(state.iconRes)
        binding.placeholderTitle.text = state.title
        binding.errorSubtitle.isVisible = false
        binding.retryButton.isVisible = false

        hideHistory()
    }

    private fun showError(state: SearchState.Error) {
        binding.progressBar.isVisible = false
        binding.rvTracks.isVisible = false
        binding.placeholderContainer.isVisible = true

        binding.placeholderImage.setImageResource(state.iconRes)
        binding.placeholderTitle.text = state.title
        binding.errorSubtitle.isVisible = state.subtitle != null
        state.subtitle?.let { binding.errorSubtitle.text = it }
        binding.retryButton.isVisible = state.showRetry

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacksAndMessages(null)
    }
}