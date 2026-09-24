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
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val url: String
) : ViewModel() {

    private val mediaPlayer = MediaPlayer()
    private var playerState = AudioPlayerState.DEFAULT
//LiveData состояние плеера
    private val stateLiveData = MutableLiveData(AudioPlayerState.DEFAULT)
    fun observeState(): LiveData<AudioPlayerState> = stateLiveData
//Прогресс воспроизведения
    private val progressLiveData = MutableLiveData(DEFAULT_TIME)
    fun observeProgress(): LiveData<String> = progressLiveData

    private val handler = Handler(Looper.getMainLooper())
    private val progressRunnable = object : Runnable {
        override fun run() {
            if (playerState == AudioPlayerState.PLAYING) {
                progressLiveData.postValue(formatTime(mediaPlayer.currentPosition))
                handler.postDelayed(this, UPDATE_INTERVAL)
            }
        }
    }

    init {
        preparePlayer(url)
    }

    fun playPause() {
        when (playerState) {
            AudioPlayerState.PLAYING -> pausePlayer()
            AudioPlayerState.PREPARED, AudioPlayerState.PAUSED -> startPlayer()
            AudioPlayerState.DEFAULT -> Log.d(TAG, "Player isn't ready")
        }
    }

    fun onPause() {
        if (playerState == AudioPlayerState.PLAYING) {
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
                playerState = AudioPlayerState.PREPARED
                stateLiveData.postValue(playerState)
            }
//трек доиграл. сбрасываем прогресс
            mediaPlayer.setOnCompletionListener {
                playerState = AudioPlayerState.PREPARED
                stateLiveData.postValue(playerState)
                progressLiveData.postValue(DEFAULT_TIME)
                stopProgressUpdates()
            }
//При ошибке
            mediaPlayer.setOnErrorListener { _, what, extra ->
                Log.e(TAG, "MediaPlayer error: what=$what, extra=$extra")
                playerState = AudioPlayerState.DEFAULT
                stateLiveData.postValue(playerState)
                true
            }
//Асинхонная подготовка
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to prepare MediaPlayer", e)
            playerState = AudioPlayerState.DEFAULT
            stateLiveData.postValue(playerState)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = AudioPlayerState.PLAYING
        stateLiveData.postValue(playerState)
        startProgressUpdates()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = AudioPlayerState.PAUSED
        stateLiveData.postValue(playerState)
        stopProgressUpdates()
    }

    private fun startProgressUpdates() {
        handler.post(progressRunnable)
    }

    private fun stopProgressUpdates() {
        handler.removeCallbacks(progressRunnable)
    }

    private fun formatTime(millis: Int): String {
        return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(millis)
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
        private const val DEFAULT_TIME = "00:00"
//Фабрика создания AudioPlayerViewModel
        fun getFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AudioPlayerViewModel(url)
            }
        }
    }
}