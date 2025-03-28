package com.example.weatherapp.data.repo

import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class Repo ( private val remoteDataSource: WeatherRemoteDataSource){
    suspend fun getCurrentWeather(isOnline:Boolean, city:String, lang:String,unit:String): Flow<WeatherData> {
        return remoteDataSource.getCurrentWeather(city=city, lang=lang,unit = unit)

    }
    suspend fun getCurrentWeatherByCoord(isOnline:Boolean, lat:Double,lon:Double, lang:String,unit:String): Flow<WeatherData> {
        return remoteDataSource.getCurrentWeatherByCoord(lat=lat,lon=lon, lang=lang,unit = unit)

    }
    suspend fun getForecast(isOnline:Boolean, city:String, lang:String,unit:String):Flow<ForecastData> {
        return remoteDataSource.getForecast(city=city, lang=lang,unit = unit)

    }
    suspend fun getForecastByCoord(isOnline:Boolean, lat:Double,lon:Double, lang:String,unit:String): Flow<ForecastData> {
        return remoteDataSource.getForecastByCoord(lat=lat,lon=lon, lang=lang,unit = unit)

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