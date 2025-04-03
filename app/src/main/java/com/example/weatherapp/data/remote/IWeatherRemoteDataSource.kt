package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import kotlinx.coroutines.flow.Flow

interface IWeatherRemoteDataSource {

    suspend fun getCurrentWeather(city: String ,
                                  lang: String ,
                                  unit: String ): Flow<WeatherData>

    suspend fun getCurrentWeatherByCoord(lat: Double ,
                                         lon: Double ,
                                         lang: String,
                                         unit: String ): Flow<WeatherData>

    suspend fun getForecast(city: String ,
                            lang: String ,
                            unit: String ): Flow<ForecastData>

    suspend fun getForecastByCoord(lat: Double ,
                                   lon: Double ,
                                   lang: String ,
                                   unit: String ): Flow<ForecastData>
}