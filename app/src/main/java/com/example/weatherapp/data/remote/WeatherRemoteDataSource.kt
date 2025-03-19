package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData

class WeatherRemoteDataSource(private  val service: WeatherServices) {
    suspend fun getCurrentWeather(): WeatherData {
        return service.getWeather("cairo","e13a916deb17bb538fdfabaf0d57e6a5")
    }
    suspend fun getForecast(): ForecastData {
        return service.getForecast("cairo","e13a916deb17bb538fdfabaf0d57e6a5")
    }
}