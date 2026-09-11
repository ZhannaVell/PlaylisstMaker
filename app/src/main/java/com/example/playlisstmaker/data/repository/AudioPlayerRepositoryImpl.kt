package com.example.playlisstmaker.data.repository

import android.media.MediaPlayer
import com.example.playlisstmaker.data.media.MediaPlayerManager
import com.example.playlisstmaker.data.media.ProgressTimer
import com.example.playlisstmaker.domain.api.AudioPlayerRepository

class AudioPlayerRepositoryImpl(
    private val mediaPlayerManager: MediaPlayerManager,

) : AudioPlayerRepository{

    override fun prepare(url: String) {
        mediaPlayerManager.prepare(url)
    }

    override fun start() {
        mediaPlayerManager.start()

    }

    override fun pause() {
        mediaPlayerManager.pause()

    }

    override fun release() {

        mediaPlayerManager.release()
    }
    override fun getState(): Int = mediaPlayerManager.getState()

    override fun isPlaying(): Boolean = mediaPlayerManager.isPlaying()
    override fun getPlayer(): MediaPlayer? = mediaPlayerManager.getPlayer()

}