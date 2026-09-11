package com.example.playlisstmaker.domain.api

import android.media.MediaPlayer
import com.example.playlisstmaker.domain.models.Track

interface AudioPlayerRepository {
    fun prepare(url: String)
    fun start()
    fun pause()
    fun release ()
    fun getState() : Int
    fun isPlaying() : Boolean
    fun getPlayer(): MediaPlayer?

}