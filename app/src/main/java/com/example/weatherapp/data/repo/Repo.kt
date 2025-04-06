package com.example.weatherapp.data.repo

import com.example.weatherapp.data.local.CityLocalDataSource
import com.example.weatherapp.data.local.ICityLocalDataSource
import com.example.weatherapp.data.model.FavCity
import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.remote.IWeatherRemoteDataSource
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class Repo (private val remoteDataSource: IWeatherRemoteDataSource,
            private val localDataSource: ICityLocalDataSource
){
    suspend fun getCurrentWeather( city:String, lang:String,unit:String): Flow<WeatherData> {
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

    //local
    suspend fun getFavCity():Flow<List<FavCity>>{
        return   localDataSource.getAll()

    }
    suspend fun insert(city: FavCity):Long{
        return localDataSource.insertCity(city)
    }
    suspend fun delete(city: FavCity):Int{
        return localDataSource.delete(city)

    }


}