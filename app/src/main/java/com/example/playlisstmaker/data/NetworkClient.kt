package com.example.playlisstmaker.data

import com.example.playlisstmaker.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}