package com.example.playlisstmaker.search.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.playlisstmaker.search.domain.model.Track
import com.example.playlisstmaker.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SearchHistoryDataStore(private val context: Context) {

    private val gson = Gson()
    private val listType = object : TypeToken<List<Track>>() {}.type


    val historyFlow: Flow<List<Track>> = context.searchHistoryDataStore.data
        .map { preferences ->
            val json = preferences[HISTORY_KEY] ?: return@map emptyList()
            gson.fromJson<List<Track>>(json, listType) ?: emptyList()
        }


    suspend fun addTrack(track: Track) {
        context.searchHistoryDataStore.edit { preferences ->
            val currentJson = preferences[HISTORY_KEY]
            val current = gson.fromJson<List<Track>>(currentJson, listType) ?: emptyList()

            val updated = buildList {
                add(track)
                addAll(current.filter { it.trackId != track.trackId })
            }.take(Constants.MAX_HISTORY_SIZE)

            preferences[HISTORY_KEY] = gson.toJson(updated)
        }
    }


    suspend fun clearHistory() {
        context.searchHistoryDataStore.edit { preferences ->
            preferences.remove(HISTORY_KEY)
        }
    }

    companion object {
        private val Context.searchHistoryDataStore: DataStore<Preferences> by preferencesDataStore(
            name = Constants.SEARCH_HISTORY_PREFERENCES
        )

        private val HISTORY_KEY = stringPreferencesKey(Constants.HISTORY_KEY)
    }
}