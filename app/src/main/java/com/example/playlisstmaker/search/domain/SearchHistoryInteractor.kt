package com.example.playlisstmaker.search.domain

import com.example.playlisstmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {
    val historyFlow: Flow<List<Track>>
    suspend fun addTrack(track: Track)
    suspend fun clearHistory()
}