package com.example.playlisstmaker.search.domain

import com.example.playlisstmaker.search.domain.model.Track

interface SearchTracksInteractor {
    suspend fun searchTracks(expression: String): List<Track>
}