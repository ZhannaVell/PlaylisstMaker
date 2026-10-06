package com.example.playlisstmaker.utils

import android.content.Context

class AndroidResourceProvider(
    private val context: Context
) : ResourceProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}