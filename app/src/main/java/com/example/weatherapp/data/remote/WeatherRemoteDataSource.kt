package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSource(private  val service: WeatherServices) {
    suspend fun getCurrentWeather(): Flow<WeatherData> {
        return flow {
            val response = service.getWeather("cairo","e13a916deb17bb538fdfabaf0d57e6a5","ar")
          emit(response)
        }

       // return service.getWeather("cairo","e13a916deb17bb538fdfabaf0d57e6a5","ar")
    }
    suspend fun getForecast(): Flow<ForecastData> {
        return flow {
            val response = service.getForecast("cairo","e13a916deb17bb538fdfabaf0d57e6a5","ar")
            emit(response)
        }
    }
}