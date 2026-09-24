package com.example.playlisstmaker.search.data.repository


import com.example.playlisstmaker.search.data.SearchHistoryDataStore
import com.example.playlisstmaker.search.domain.SearchHistoryRepository
import com.example.playlisstmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class SearchHistoryRepositoryImpl(
   private val dataStore: SearchHistoryDataStore
) : SearchHistoryRepository {


    override val historyFlow: Flow<List<Track>> = dataStore.historyFlow

    override suspend fun addTrack(track: Track) {
        dataStore.addTrack(track)
    }

    override suspend fun clearHistory() {
        dataStore.clearHistory()
    }
}