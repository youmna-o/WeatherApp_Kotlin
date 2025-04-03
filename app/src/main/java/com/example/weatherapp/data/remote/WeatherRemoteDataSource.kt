package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSource(private  val service: WeatherServices,
                               var lat :Double=31.0797867,
                               var lon:Double=31.590905,
                               var city:String ="cairo",
                               var lang :String="en",
                               var unit :String="metric") : IWeatherRemoteDataSource {
     var apiKey ="e13a916deb17bb538fdfabaf0d57e6a5"

    override suspend fun getCurrentWeather(city: String,
                                           lang: String,
                                           unit: String
    ): Flow<WeatherData> {
        return flow {
            val response = service.getWeather(city=city,apiKey=apiKey,lang=lang,unit=unit)
          emit(response)
        }

    }
    override suspend fun getCurrentWeatherByCoord(lat: Double,
                                                  lon: Double,
                                                  lang: String,
                                                  unit: String
    ): Flow<WeatherData> {
        return flow {
            val response = service.getWeatherByCoord(lat=lat,lon=lon,apiKey=apiKey,lang=lang,unit=unit)
            emit(response)
        }

    }
    override suspend fun getForecast(city: String,
                                     lang: String,
                                     unit: String
    ): Flow<ForecastData> {
        return flow {
            val response = service.getForecast(city=city,apiKey=apiKey,lang=lang,unit=unit)
            emit(response)
        }

    }
    override suspend fun getForecastByCoord(lat: Double,
                                            lon: Double,
                                            lang: String,
                                            unit: String
    ): Flow<ForecastData> {
        return flow {
            val response = service.getForecastByCoords(
                lat = lat,
                lon = lon,
                apiKey = apiKey,
                lang = lang,
                unit = unit
            )
            emit(response)
        }
    }
}