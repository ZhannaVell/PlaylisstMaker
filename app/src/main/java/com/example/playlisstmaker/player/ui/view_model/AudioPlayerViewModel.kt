package com.example.playlisstmaker.player.ui.view_model


import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlisstmaker.player.domain.model.AudioPlayerState
import com.example.playlisstmaker.player.ui.state.PlayerScreenState
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val url: String
) : ViewModel() {

    private val mediaPlayer = MediaPlayer()
    private var playerState: AudioPlayerState = AudioPlayerState.Default

    //LiveData состояние плеера
    private val playerStateLiveData = MutableLiveData(PlayerScreenState())
    fun observeState(): LiveData<PlayerScreenState> = playerStateLiveData


    private val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())

    private val handler = Handler(Looper.getMainLooper())
    private val progressRunnable = object : Runnable {
        override fun run() {
            if (playerState == AudioPlayerState.Playing) {
                updateState(progress = formatTime(mediaPlayer.currentPosition))
                handler.postDelayed(this, UPDATE_INTERVAL)
            }
        }
    }

    init {
        preparePlayer(url)
    }

    fun playPause() {
        when (playerState) {
            AudioPlayerState.Playing -> pausePlayer()
            AudioPlayerState.Prepared, AudioPlayerState.Paused -> startPlayer()
            AudioPlayerState.Default -> Log.d(TAG, "Player isn't ready")
        }
    }

    fun onPause() {
        if (playerState == AudioPlayerState.Playing) {
            pausePlayer()
        }
    }

    private fun preparePlayer(url: String) {
        if (url.isEmpty()) {
            Log.d(TAG, "Track doesn't contain previewUrl")
            return
        }

        try {
            mediaPlayer.setDataSource(url)
//Плеер готов
            mediaPlayer.setOnPreparedListener {
                playerState = AudioPlayerState.Prepared
                updateState()
            }
//трек доиграл. сбрасываем прогресс
            mediaPlayer.setOnCompletionListener {
                playerState = AudioPlayerState.Prepared
                updateState(progress = PlayerScreenState.DEFAULT_TIME)
                stopProgressUpdates()
            }
//При ошибке
            mediaPlayer.setOnErrorListener { _, what, extra ->
                Log.e(TAG, "MediaPlayer error: what=$what, extra=$extra")
                playerState = AudioPlayerState.Default
                updateState()
                true
            }
//Асинхонная подготовка
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to prepare MediaPlayer", e)
            playerState = AudioPlayerState.Default
            updateState()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = AudioPlayerState.Playing
        updateState()
        startProgressUpdates()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = AudioPlayerState.Paused
        updateState()
        stopProgressUpdates()
    }

    private fun startProgressUpdates() {
        handler.post(progressRunnable)
    }

    private fun stopProgressUpdates() {
        handler.removeCallbacks(progressRunnable)
    }

    private fun formatTime(millis: Int): String {
        return timeFormatter.format(millis)
    }

    private fun updateState(
        state: AudioPlayerState = playerState,
        progress: String = playerStateLiveData.value?.progressTime
            ?: PlayerScreenState.DEFAULT_TIME
    ) {
        playerStateLiveData.postValue(PlayerScreenState(state, progress))
    }

    override fun onCleared() {
        super.onCleared()
        stopProgressUpdates()
        mediaPlayer.release()
        Log.d(TAG, "ViewModel cleared, MediaPlayer released")
    }

    companion object {
        private const val TAG = "AudioPlayerViewModel"
        private const val UPDATE_INTERVAL = 300L
        private const val TIME_FORMAT = "mm:ss"

        //Фабрика создания AudioPlayerViewModel
        fun getFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AudioPlayerViewModel(url)
            }
        }
    }
}