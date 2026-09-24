package com.example.playlisstmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlisstmaker.creator.Creator
import com.example.playlisstmaker.search.domain.SearchHistoryInteractor
import com.example.playlisstmaker.search.domain.SearchTracksInteractor
import com.example.playlisstmaker.search.domain.model.Track
import com.example.playlisstmaker.search.ui.state.SearchState
import com.example.playlisstmaker.utils.Constants
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
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
                        SearchState.Empty
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
                    if (tracks.isNotEmpty()) SearchState.Content(tracks) else SearchState.Empty
                )
            } catch (e: Exception) {
                stateLiveData.postValue(SearchState.Error)
            }
        }
    }

    private fun updateState() {
        when {
            stateLiveData.value is SearchState.Loading -> return
            query.isEmpty() && hasFocus && currentHistory.isNotEmpty() ->
                stateLiveData.value = SearchState.History(currentHistory)
            query.isEmpty() ->
                stateLiveData.value = SearchState.Empty
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    searchTracksInteractor = Creator.provideSearchTracksInteractor(),
                    searchHistoryInteractor = Creator.provideSearchHistoryInteractor()
                )
            }
        }
    }
}


