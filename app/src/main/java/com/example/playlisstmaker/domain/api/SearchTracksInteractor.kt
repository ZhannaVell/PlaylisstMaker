package com.example.playlisstmaker.domain.api

import com.example.playlisstmaker.domain.models.Track

interface SearchTracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
            fun onError()
        }
    }
