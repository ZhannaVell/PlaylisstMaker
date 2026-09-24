package com.example.playlisstmaker.search.data.repository

import com.example.playlisstmaker.search.data.dto.Response
import com.example.playlisstmaker.search.domain.TracksRepository
import com.example.playlisstmaker.search.domain.model.Track
import com.example.playlisstmaker.search.data.dto.SearchTracksRequest
import com.example.playlisstmaker.search.data.dto.TrackResponse
import com.example.playlisstmaker.search.data.network.NetworkClient
import com.example.playlisstmaker.utils.Constants

class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(SearchTracksRequest(expression))

        return if (response.resultCode == Response.SUCCESS_CODE && response is TrackResponse) {
            response.results.map { dto ->
                Track(
                    trackId = dto.trackId,
                    trackName = dto.trackName ?: Constants.UNKNOWN_VALUE,
                    artistName = dto.artistName ?: Constants.UNKNOWN_VALUE,
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