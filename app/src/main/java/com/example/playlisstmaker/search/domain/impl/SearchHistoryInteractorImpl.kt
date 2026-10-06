package com.example.playlisstmaker.search.domain.impl

import com.example.playlisstmaker.search.domain.model.Track
import com.example.playlisstmaker.search.domain.SearchHistoryInteractor
import com.example.playlisstmaker.search.domain.SearchHistoryRepository

import kotlinx.coroutines.flow.Flow

class SearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : SearchHistoryInteractor {
    override val historyFlow: Flow<List<Track>> = repository.historyFlow

    override suspend fun addTrack(track: Track) {

        repository.addTrack(track)
    }

    override suspend fun clearHistory() = repository.clearHistory()

}