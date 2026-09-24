package com.example.playlisstmaker.search.domain.impl

import com.example.playlisstmaker.search.domain.model.Track
import com.example.playlisstmaker.search.domain.SearchTracksInteractor
import com.example.playlisstmaker.search.domain.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchTracksInteractorImpl(
    private val repository: TracksRepository
) : SearchTracksInteractor {

    override suspend fun searchTracks(expression: String): List<Track> {
        return withContext(Dispatchers.IO) {
            repository.searchTracks(expression)
        }
    }
}