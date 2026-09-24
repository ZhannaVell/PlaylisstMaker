package com.example.playlisstmaker.search.data.network

import com.example.playlisstmaker.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(request: NetworkRequest): Response
}