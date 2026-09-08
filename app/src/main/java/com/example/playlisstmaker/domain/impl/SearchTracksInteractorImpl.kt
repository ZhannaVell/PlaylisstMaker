package com.example.playlisstmaker.domain.impl

import com.example.playlisstmaker.domain.api.SearchTracksInteractor
import com.example.playlisstmaker.domain.api.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchTracksInteractorImpl (
    private val repository: TracksRepository
    ) : SearchTracksInteractor {

    override fun searchTracks(expression: String, consumer: SearchTracksInteractor.TracksConsumer) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val tracks = repository.searchTracks(expression)
                withContext(Dispatchers.Main) {
                    consumer.consume(tracks)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    consumer.onError()
                }
            }
        }
    }
}
