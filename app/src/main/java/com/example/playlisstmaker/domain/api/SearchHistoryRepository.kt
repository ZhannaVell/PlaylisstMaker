package com.example.playlisstmaker.domain.api

import com.example.playlisstmaker.domain.models.Track

interface SearchHistoryRepository {
    fun getHistory(): List <Track>
    fun saveHistory(history: List<Track>)
    fun clearHistory()
}