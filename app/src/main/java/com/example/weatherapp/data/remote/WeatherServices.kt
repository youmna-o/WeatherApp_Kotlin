package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {
    @GET("weather")
    suspend fun getWeather(@Query("q")city: String,@Query("appid")apiKey: String): WeatherData
    @GET("forecast")
    suspend fun getForecast(@Query("q")city: String,@Query("appid")apiKey: String): ForecastData
}