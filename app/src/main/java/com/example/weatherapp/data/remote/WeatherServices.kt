package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {
    @GET("weather") //kelvin default
    suspend fun getWeather(@Query("q")city: String,@Query("appid")apiKey: String,@Query("lang")lang: String,@Query("units")unit: String): WeatherData
    @GET("weather") //kelvin default
    suspend fun getWeatherByCoord(@Query("lat")lat: Double,@Query("lon")lon: Double,@Query("appid")apiKey: String,@Query("lang")lang: String,@Query("units")unit: String): WeatherData
   //(Fahrenheit use units=imperial , Celsius use units=metric)
    @GET("forecast")
    suspend fun getForecast(@Query("q")city: String,@Query("appid")apiKey: String,@Query("lang")lang: String,@Query("units")unit: String): ForecastData
    @GET("forecast")
    suspend fun getForecastByCoords(@Query("lat")lat: Double,@Query("lon")lon: Double,@Query("appid")apiKey: String,@Query("lang")lang: String,@Query("units")unit: String): ForecastData


}