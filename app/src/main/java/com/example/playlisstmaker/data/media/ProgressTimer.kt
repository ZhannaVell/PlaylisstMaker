package com.example.playlisstmaker.data.media

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.playlisstmaker.domain.models.AudioPlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class ProgressTimer (
    private val onTimeUpdate: (String) -> Unit
    ){
    companion object {
        private const val UPDATE_INTERVAL = 300L
        private const val TAG = "ProgressTimer"
    }
    private val handler = Handler(Looper.getMainLooper())
    private val audioRunnable = Runnable {
        updateCurrentTime()
    }
    private var playerState = AudioPlayerState.DEFAULT
    private var mediaPlayer: MediaPlayer? = null

    fun update(player: MediaPlayer?, state: Int) {
        mediaPlayer = player
        playerState = state
    }
    private fun updateCurrentTime() {
        if (playerState == AudioPlayerState.PLAYING) {
            val player = mediaPlayer ?: return
            val currentPosition = player.currentPosition
            onTimeUpdate(formatTime(currentPosition))
            handler.postDelayed(audioRunnable, UPDATE_INTERVAL)
        }
    }


    private fun formatTime(millis: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }


    fun start() {
        Log.d(TAG, "Timer started")
        handler.post(audioRunnable)
    }


    fun stop() {
        Log.d(TAG, "Timer stopped")
        handler.removeCallbacks(audioRunnable)
    }





}