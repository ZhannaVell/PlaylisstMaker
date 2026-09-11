package com.example.playlisstmaker.domain.impl

import com.example.playlisstmaker.data.media.MediaPlayerManager
import com.example.playlisstmaker.data.media.ProgressTimer
import com.example.playlisstmaker.domain.api.AudioPlayerInteractor
import com.example.playlisstmaker.domain.api.AudioPlayerRepository

class AudioPlayerInteractorImpl(
    private val audioPlayerRepository: AudioPlayerRepository,
    private val progressTimer: ProgressTimer
) : AudioPlayerInteractor {

    override fun prepare(url: String) {
        audioPlayerRepository.prepare(url)
    }

    override fun start() {
        audioPlayerRepository.start()
        progressTimer.update(audioPlayerRepository.getPlayer(), audioPlayerRepository.getState())
        progressTimer.start()
    }

    override fun pause() {
        audioPlayerRepository.pause()
        progressTimer.stop()

    }

    override fun release() {
        progressTimer.stop()
        audioPlayerRepository.release()
    }

    override fun getState(): Int {
        return audioPlayerRepository.getState()
    }

    override fun isPlaying(): Boolean {
        return audioPlayerRepository.isPlaying()
    }



}