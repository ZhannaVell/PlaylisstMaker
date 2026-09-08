package com.example.playlisstmaker.data.repository

import com.example.playlisstmaker.data.NetworkClient
import com.example.playlisstmaker.data.dto.SearchTracksRequest
import com.example.playlisstmaker.data.dto.TrackResponse
import com.example.playlisstmaker.domain.api.TracksRepository
import com.example.playlisstmaker.domain.models.Track

class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(SearchTracksRequest(expression))

        return if (response.resultCode == 200 && response is TrackResponse) {
            response.results.map { dto ->
                Track(
                    trackId = dto.trackId,
                    trackName = dto.trackName ?: "Unknown",
                    artistName = dto.artistName ?: "Unknown",
                    trackTime = formatTime(dto.trackTimeMillis ?: 0),
                    artworkUrl100 = dto.artworkUrl100 ?: "",
                    collectionName = dto.collectionName,
                    releaseDate = dto.releaseDate,
                    primaryGenreName = dto.primaryGenreName,
                    country = dto.country,
                    previewUrl = dto.previewUrl
                )
            }
        } else {
            emptyList()
        }
    }

    private fun formatTime(millis: Long): String {
        val totalSeconds = (millis / 1000).toInt()
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }
}