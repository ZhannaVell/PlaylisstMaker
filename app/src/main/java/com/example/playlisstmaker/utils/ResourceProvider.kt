package com.example.playlisstmaker.utils

interface ResourceProvider {
    fun getString(resId: Int) : String
}