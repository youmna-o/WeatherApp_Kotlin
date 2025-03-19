package com.example.weatherapp.data.repo

import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.remote.WeatherRemoteDataSource

class Repo ( private val remoteDataSource: WeatherRemoteDataSource){
    suspend fun getCurrentWeather():WeatherData{
        return remoteDataSource.getCurrentWeather()
    }
    suspend fun getForecast():ForecastData{
        return remoteDataSource.getForecast()
    }
    companion object{
        @Volatile
        private var instance : Repo? = null
        fun getInstance(remoteDataSource: WeatherRemoteDataSource): Repo {
            return instance ?: synchronized(this){
                val temp= Repo (remoteDataSource)
                instance = temp
                temp
            }

        }
    }
}