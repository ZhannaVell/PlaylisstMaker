package com.example.playlisstmaker.data.network

import com.example.playlisstmaker.data.NetworkClient
import com.example.playlisstmaker.data.dto.Response
import com.example.playlisstmaker.data.dto.SearchTracksRequest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {


    private val baseUrl = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(iTunesApi::class.java)

    override suspend fun doRequest(dto: Any): Response {
        return when (dto) {
            is SearchTracksRequest -> {
                try {
                    val response = apiService.searchTracks(term = dto.expression, entity = "song")

                    response.apply { resultCode = 200 }
                } catch (e: Exception) {
                    // Если ошибка — возвращаем пустой ответ с кодом 400
                    Response().apply { resultCode = 400 }
                }
            }
            else -> {
                Response().apply { resultCode = 400 }
            }
        }
    }
}
