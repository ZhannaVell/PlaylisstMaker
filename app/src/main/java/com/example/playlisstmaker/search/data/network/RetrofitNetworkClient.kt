package com.example.playlisstmaker.search.data.network

import com.example.playlisstmaker.search.data.dto.Response
import com.example.playlisstmaker.search.data.dto.SearchTracksRequest
import com.example.playlisstmaker.utils.Constants.ENTITY_SONG
import com.example.playlisstmaker.utils.Constants.ITUNES_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {


    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(iTunesApi::class.java)

    override suspend fun doRequest(request: NetworkRequest): Response {
        return when (request) {
            is SearchTracksRequest -> {
                try {
                    val response =
                        apiService.searchTracks(term = request.expression, entity = ENTITY_SONG)

                    response.apply { resultCode = Response.SUCCESS_CODE }
                } catch (e: Exception) {

                    Response().apply { resultCode = Response.ERROR_CODE }

                }
            }

            else -> Response().apply { resultCode = Response.ERROR_CODE }
        }
    }
}