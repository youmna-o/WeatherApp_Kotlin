package com.example.weatherapp.data.repo

import com.example.weatherapp.ui.ForecastData
import com.example.weatherapp.ui.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {
    @GET("weather")
    suspend fun getWeather(@Query("q")city: String,@Query("appid")apiKey: String): WeatherData
    @GET("forecast")
    suspend fun getForecast(@Query("q")city: String,@Query("appid")apiKey: String): ForecastData
}