package com.example.weatherapp.data.repo

import com.example.weatherapp.ui.WeatherData

class WeatherRemoteDataSource(private  val service: WeatherServices) {
    suspend fun getAllProduct():WeatherData{
        return service.getWeather("cairo","e13a916deb17bb538fdfabaf0d57e6a5")
    }
}