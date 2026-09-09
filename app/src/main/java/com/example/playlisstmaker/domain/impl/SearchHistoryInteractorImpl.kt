package com.example.playlisstmaker.domain.impl

import com.example.playlisstmaker.domain.api.SearchHistoryInteractor
import com.example.playlisstmaker.domain.api.SearchHistoryRepository
import com.example.playlisstmaker.domain.models.Track
import com.example.playlisstmaker.utils.Constants

class SearchHistoryInteractorImpl (
    private val repository: SearchHistoryRepository
) : SearchHistoryInteractor {




        override fun addTrack(track: Track) {
            val history = repository.getHistory()
            val updated = listOf(track) + history.filter { it.trackId != track.trackId }
            val final = updated.take(Constants.MAX_HISTORY_SIZE)
            repository.saveHistory(final)
        }

        override fun clearHistory() = repository.clearHistory()
    override fun getHistory(): List<Track> = repository.getHistory()
    }

