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
) : SearchHistoryRepository {


    override fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(Constants.HISTORY_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Track>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }
    override fun saveHistory(history: List<Track>) {
        val json = gson.toJson(history)
        sharedPreferences.edit()
            .putString(Constants.HISTORY_KEY, json)
            .apply()

    }

    override fun clearHistory() {
        saveHistory(emptyList())
    }



}