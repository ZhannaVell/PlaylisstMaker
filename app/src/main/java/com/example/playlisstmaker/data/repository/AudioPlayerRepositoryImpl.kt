package com.example.playlisstmaker.data.repository

import com.example.playlisstmaker.data.media.MediaPlayerManager
import com.example.playlisstmaker.data.media.ProgressTimer
import com.example.playlisstmaker.domain.api.AudioPlayerRepository

class AudioPlayerRepositoryImpl(
    private val mediaPlayerManager: MediaPlayerManager,
    private val progressTimer: ProgressTimer
) : AudioPlayerRepository{

    override fun prepare(url: String) {
        mediaPlayerManager.prepare(url)
    }

    override fun start() {
        mediaPlayerManager.start()
        progressTimer.update(mediaPlayerManager.getPlayer(), mediaPlayerManager.getState())
        progressTimer.start()
    }

    override fun pause() {
        mediaPlayerManager.pause()
        progressTimer.stop()
    }

    override fun release() {
        progressTimer.stop()
        mediaPlayerManager.release()
    }
    override fun getState(): Int = mediaPlayerManager.getState()

    override fun isPlaying(): Boolean = mediaPlayerManager.isPlaying()

}