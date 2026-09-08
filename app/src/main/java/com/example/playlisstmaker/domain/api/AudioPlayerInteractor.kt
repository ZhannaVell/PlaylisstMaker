package com.example.playlisstmaker.domain.api

interface AudioPlayerInteractor{

    fun prepare(url: String)

    fun start()

    fun pause()

    fun release()

    fun getState(): Int

    fun isPlaying(): Boolean


}