package com.example.playlisstmaker.data.network

import com.example.playlisstmaker.data.dto.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {
    @GET("search")
    suspend fun searchTracks(
        @Query("term") term: String,
        @Query("entity") entity: String = "song"
    ): TrackResponse
}