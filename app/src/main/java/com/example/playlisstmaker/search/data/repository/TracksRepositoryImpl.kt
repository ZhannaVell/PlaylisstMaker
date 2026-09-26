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

         if (response.resultCode == Response.ERROR_CODE) {
            throw IllegalStateException("Server error: ${response.resultCode}")
        }
        return if (response is TrackResponse) {
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
        val totalSeconds = (millis / MILLIS_IN_SECOND).toInt()
        val minutes = totalSeconds / SECONDS_IN_MINUTE
        val seconds = totalSeconds % SECONDS_IN_MINUTE
        return "%02d:%02d".format(minutes, seconds)
    }
    companion object {
        private const val MILLIS_IN_SECOND = 1000
        private const val SECONDS_IN_MINUTE = 60
        private const val TIME_FORMAT = "%02d:%02d"
    }
}