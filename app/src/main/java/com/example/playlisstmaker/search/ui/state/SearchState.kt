package com.example.playlisstmaker.search.ui.state

import com.example.playlisstmaker.search.domain.model.Track

sealed interface SearchState {
    object Loading : SearchState

    data class Content(
        val tracks: List<Track>
    ) : SearchState

    object Empty : SearchState

    object Error : SearchState

    data class History(
        val tracks: List<Track>
    ) : SearchState
}