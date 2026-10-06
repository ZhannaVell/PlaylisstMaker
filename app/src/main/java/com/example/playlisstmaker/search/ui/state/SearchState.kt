package com.example.playlisstmaker.search.ui.state


import com.example.playlisstmaker.search.domain.model.Track

sealed interface SearchState {
    object Idle : SearchState
    object Loading : SearchState

    data class Content(
        val tracks: List<Track>
    ) : SearchState

    data class Empty(
        val title: String,
        val iconRes: Int
    ) : SearchState

    data class Error(
        val title: String,
        val subtitle: String?,
        val iconRes: Int,
        val showRetry: Boolean
    ) : SearchState


    data class History(
        val tracks: List<Track>
    ) : SearchState
}