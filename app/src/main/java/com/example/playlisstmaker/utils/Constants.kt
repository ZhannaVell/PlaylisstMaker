package com.example.playlisstmaker.utils


object Constants {
    // SharedPreferences

    const val SEARCH_HISTORY_PREFERENCES = "search_history"

    const val HISTORY_KEY = "history_data"


    // Intent
    const val TRACK_EXTRA = "track"
    const val TRACK_STATE_KEY = "track_state"
    const val MIME_TYPE_TEXT_PLAIN = "text/plain"

    // Ошибки
    const val ERROR_TRACK_MISSING = "Track data is missing"

    // Debounce
    const val SEARCH_DEBOUNCE_DELAY = 2000L
    const val CLICK_DEBOUNCE_DELAY = 1000L

    // Ограничения
    const val MAX_HISTORY_SIZE = 10

    // TracksRepository
    const val UNKNOWN_VALUE = "Unknown"

    // Sharing
    const val MAILTO_PREFIX = "mailto:"

    //RetrofitNetworkClient
    const val ITUNES_BASE_URL = "https://itunes.apple.com/"
    const val ENTITY_SONG = "song"


}
