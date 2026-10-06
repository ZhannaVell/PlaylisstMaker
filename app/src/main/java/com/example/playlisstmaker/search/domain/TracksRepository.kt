package com.example.playlisstmaker.search.domain

import com.example.playlisstmaker.search.domain.model.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
}