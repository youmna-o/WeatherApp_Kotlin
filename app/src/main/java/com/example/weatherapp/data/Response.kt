package com.example.weatherapp.data

sealed class Response<out T> {
    class Loading<T> : Response<T>()
    data class Success<T>(val data: T) : Response<T>()
    data class Failure<T>(val error: Throwable) : Response<T>()
}
