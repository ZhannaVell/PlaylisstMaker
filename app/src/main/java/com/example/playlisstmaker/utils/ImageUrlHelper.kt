package com.example.playlisstmaker.utils

object ImageUrlHelper {
    fun getCoverArtwork(artworkUrl100: String): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }
}