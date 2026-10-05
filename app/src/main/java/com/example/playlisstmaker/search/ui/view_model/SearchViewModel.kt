package com.example.playlisstmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope

import com.example.playlisstmaker.R

import com.example.playlisstmaker.search.domain.SearchHistoryInteractor
import com.example.playlisstmaker.search.domain.SearchTracksInteractor
import com.example.playlisstmaker.search.domain.model.Track
import com.example.playlisstmaker.search.ui.state.SearchState

import com.example.playlisstmaker.utils.Constants
import com.example.playlisstmaker.utils.ResourceProvider
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private var query: String = ""
    private var hasFocus: Boolean = false
    private var currentHistory: List<Track> = emptyList()

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { performSearch() }

    init {
        viewModelScope.launch {
            searchHistoryInteractor.historyFlow.collect { history ->
                currentHistory = history
                if (query.isEmpty() && hasFocus) {
                    stateLiveData.value = if (history.isNotEmpty()) {
                        SearchState.History(history)
                    } else {
                        SearchState.Idle
                    }
                }
            }
        }
    }

    fun onQueryChanged(newQuery: String) {
        query = newQuery
        if (newQuery.isEmpty()) {
            updateState()
        } else {
            searchDebounce()
        }
    }

    fun onFocusChanged(focused: Boolean) {
        hasFocus = focused
        if (query.isEmpty()) {
            updateState()
        }
    }

    fun onClearQueryClicked() {
        query = ""
        updateState()
    }

    fun onRetryClicked() {
        performSearch()
    }

    fun onTrackClicked(track: Track) {
        viewModelScope.launch { searchHistoryInteractor.addTrack(track) }
    }

    fun onHistoryTrackClicked(track: Track) {
        viewModelScope.launch { searchHistoryInteractor.addTrack(track) }
    }

    fun onClearHistoryClicked() {
        viewModelScope.launch { searchHistoryInteractor.clearHistory() }
    }

    fun getQuery(): String = query
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, Constants.SEARCH_DEBOUNCE_DELAY)
    }

    private fun performSearch() {
        val currentQuery = query.trim()
        if (currentQuery.isEmpty()) return

        stateLiveData.postValue(SearchState.Loading)

        viewModelScope.launch {
            try {
                val tracks = searchTracksInteractor.searchTracks(currentQuery)
                stateLiveData.postValue(
                    if (tracks.isNotEmpty()) {
                        SearchState.Content(tracks)
                    } else {
                        SearchState.Empty(
                            title = resourceProvider.getString(R.string.empty_result),
                            iconRes = R.drawable.ic_error_empty_120
                        )
                    }
                )
            } catch (e: Exception) {
                stateLiveData.postValue(
                    SearchState.Error(
                        title = resourceProvider.getString(R.string.error_network_title),
                        subtitle = resourceProvider.getString(R.string.error_network_subtitle),
                        iconRes = R.drawable.ic_error_network_120,
                        showRetry = true
                    )
                )
            }
        }
    }
    private fun updateState() {
        when {
            stateLiveData.value is SearchState.Loading -> return
            query.isEmpty() && hasFocus && currentHistory.isNotEmpty() ->
                stateLiveData.value = SearchState.History(currentHistory)
            query.isEmpty() ->
                stateLiveData.value = SearchState.Idle
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }


}


