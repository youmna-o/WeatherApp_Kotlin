package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {
    @GET("weather") //kelvin
    suspend fun getWeather(@Query("q")city: String,@Query("appid")apiKey: String,@Query("lang")lang: String,@Query("units")unit: String): WeatherData
    /*@GET("weather") //kelvin
    suspend fun getWeather(@Query("q")city: String,@Query("appid")apiKey: String): WeatherData*/
    @GET("weather")
    suspend fun getWeatherArabic(@Query("q")city: String,@Query("appid")apiKey: String,@Query("lang")lang: String): WeatherData
    @GET("weather")//(Fahrenheit use units=imperial , Celsius use units=metric)
    suspend fun getWeatherCelsius(@Query("q")city: String,@Query("appid")apiKey: String,@Query("units")unit: String): WeatherData
    @GET("forecast")
    suspend fun getForecast(@Query("q")city: String,@Query("appid")apiKey: String): ForecastData
    @GET("forecast")
    suspend fun getForecastArabic(@Query("q")city: String,@Query("appid")apiKey: String,@Query("lang")lang: String): ForecastData

}