package com.example.playlisstmaker.domain.api

import com.example.playlisstmaker.domain.models.Track

interface SearchTracksInteractor {
    suspend fun searchTracks(expression: String): List<Track>
}
