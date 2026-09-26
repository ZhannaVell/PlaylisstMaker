package com.example.playlisstmaker.player.domain.model

//Default - плеер не готов PREPARED - готов  PLAYING - воспроизведение PAUSED - пауза. Использую в AudioPlayerViewModel для хранения и в AudioPlayerActivity для иконок плей/пауза
sealed interface AudioPlayerState {
    object Default : AudioPlayerState
    object Prepared : AudioPlayerState
    object Playing : AudioPlayerState
    object Paused : AudioPlayerState

}