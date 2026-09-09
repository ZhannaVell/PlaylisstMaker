package com.example.playlisstmaker.domain.impl

import com.example.playlisstmaker.domain.api.SearchTracksInteractor
import com.example.playlisstmaker.domain.api.TracksRepository
import com.example.playlisstmaker.domain.models.Track

import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext

class SearchTracksInteractorImpl (
    private val repository: TracksRepository
    ) : SearchTracksInteractor {

    override suspend fun searchTracks(expression: String) : List<Track> {
        return withContext(Dispatchers.IO) {
            repository.searchTracks(expression)
        }
    }
}
