package com.example.playlisstmaker.search.data.dto
//Класс для всех ответов от API
open class Response {
    var resultCode = DEFAULT_RESULT_CODE

    companion object {
        const val DEFAULT_RESULT_CODE = 0
        const val SUCCESS_CODE = 200
        const val ERROR_CODE = 400
    }
}