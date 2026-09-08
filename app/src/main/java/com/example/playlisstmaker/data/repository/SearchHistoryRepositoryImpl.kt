package com.example.playlisstmaker.data.repository

import android.content.SharedPreferences
import com.example.playlisstmaker.domain.api.SearchHistoryInteractor
import com.example.playlisstmaker.domain.api.SearchHistoryRepository

import com.example.playlisstmaker.utils.Constants
import com.example.playlisstmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SearchHistoryRepository, SearchHistoryInteractor {


    override fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(Constants.HISTORY_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Track>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } else {
            emptyList()

        }
    }

    override fun addTrack(track: Track) {
        var history = getHistory().toMutableList()
        history.removeAll { it.trackId == track.trackId }
        history.add(0, track)

        if (history.size > Constants.MAX_HISTORY_SIZE) {
            history = history.take(Constants.MAX_HISTORY_SIZE).toMutableList()
        }
        saveHistory(history)
    }

    override fun clearHistory() {
        saveHistory(emptyList())
    }

    private fun saveHistory(history: List<Track>) {
        val json = gson.toJson(history)
        sharedPreferences.edit()
            .putString(Constants.HISTORY_KEY, json)
            .apply()

    }

}