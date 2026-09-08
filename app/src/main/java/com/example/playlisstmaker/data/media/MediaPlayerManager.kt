package com.example.playlisstmaker.data.media

import android.media.MediaPlayer
import android.util.Log
import com.example.playlisstmaker.domain.models.AudioPlayerState

class MediaPlayerManager(
    private val onPrepared: () -> Unit,
    private val onCompletion: () -> Unit,
    private val onError: () -> Unit,
    private val onStateChanged: (Int) -> Unit
) {
    private var mediaPlayer: MediaPlayer? = null
    private var playerState = AudioPlayerState.DEFAULT

    companion object {
        private const val TAG = "MediaPlayerManager"
    }

        fun prepare(url: String) {
            if (url.isEmpty()) {
                Log.d(TAG, "Track doesn't contain previewUrl")
                onError()
                return
            }
            release()

            val player = MediaPlayer()
            mediaPlayer = player

            try {

                player.setOnPreparedListener {

                    playerState = AudioPlayerState.PREPARED
                    onPrepared()
                    onStateChanged(playerState)
                }

                player.setOnCompletionListener {
                    playerState = AudioPlayerState.PREPARED
                    onCompletion()
                    onStateChanged(playerState)

                }

                player.setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "MediaPlayer error: what=$what, extra=$extra")

                    playerState = AudioPlayerState.DEFAULT
                    onError()
                    onStateChanged(playerState)
                    true
                }

                player.setDataSource(url)
                player.prepareAsync()

            } catch (e: Exception) {
                Log.e(TAG, "Failed to prepare MediaPlayer", e)

                release()
            }
        }

        fun start() {
            val player = mediaPlayer ?: return
            if (!player.isPlaying) {

                player.start()
            }
            playerState = AudioPlayerState.PLAYING
            onStateChanged(playerState)
        }

        fun pause() {
            val player = mediaPlayer ?: return

            if (player.isPlaying) {
                player.pause()
            }
            playerState = AudioPlayerState.PAUSED
            onStateChanged(playerState)
        }

        fun release() {
            val player = mediaPlayer
            if (player != null) {
                try {
                    if (player.isPlaying) {
                        player.stop()
                    }
                    player.release()
                } catch (e: Exception) {
                    Log.e(TAG, "Error releasing MediaPlayer", e)
                }
                mediaPlayer = null
            }
            playerState = AudioPlayerState.DEFAULT
            onStateChanged(playerState)
        }

        fun getState(): Int = playerState
        fun isPlaying(): Boolean = mediaPlayer?.isPlaying == true
    fun getPlayer(): MediaPlayer? = mediaPlayer

    }