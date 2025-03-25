package com.example.weatherapp.data.repo

import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class Repo ( private val remoteDataSource: WeatherRemoteDataSource){
    suspend fun getCurrentWeather(isOnline:Boolean, city:String): Flow<WeatherData> {
        return if(isOnline)
            remoteDataSource.getCurrentWeather(city=city, unit = "metric")
        else{
            remoteDataSource.getCurrentWeather()
        }
    }
    suspend fun getForecast(isOnline:Boolean):Flow<ForecastData> {
        return if (isOnline)
            remoteDataSource.getForecast()
        else {
            remoteDataSource.getForecast()
        }
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