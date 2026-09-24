package com.example.playlisstmaker.search.data.dto

import com.google.gson.annotations.SerializedName
//DTO ответа от iTunes API на поиск треков

data class TrackResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val results: List<TrackDto>
) : Response()