package com.example.playlisstmaker.player.ui.state

import com.example.playlisstmaker.player.domain.model.AudioPlayerState

data class PlayerScreenState(
    val playerState: AudioPlayerState = AudioPlayerState.Default,
    val progressTime: String = DEFAULT_TIME
) {
    companion object {
        const val DEFAULT_TIME = "00:00"
    }
}

