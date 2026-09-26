package com.example.playlisstmaker.search.data.dto

import com.example.playlisstmaker.search.data.network.NetworkRequest

// DTO запроса на поиск треков
data class SearchTracksRequest(
    val expression: String
) : NetworkRequest