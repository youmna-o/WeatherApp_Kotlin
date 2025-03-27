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
                              var unit :String="metric") {
    var apiKey ="e13a916deb17bb538fdfabaf0d57e6a5"

    suspend fun getCurrentWeather(city: String = this.city,
                                  lang: String = this.lang,
                                  unit: String = this.unit): Flow<WeatherData> {
        return flow {
            val response = service.getWeather(city=city,apiKey=apiKey,lang=lang,unit=unit)
          emit(response)
        }

    }
    suspend fun getCurrentWeatherByCoord(lat: Double = this.lat,
                                         lon: Double=this.lon,
                                  lang: String = this.lang,
                                  unit: String = this.unit): Flow<WeatherData> {
        return flow {
            val response = service.getWeatherByCoord(lat=lat,lon=lon,apiKey=apiKey,lang=lang,unit=unit)
            emit(response)
        }

    }
    suspend fun getForecast(city: String = this.city,
                            lang: String = this.lang,
                            unit: String = this.unit): Flow<ForecastData> {
        return flow {
            val response = service.getForecast(city=city,apiKey=apiKey,lang=lang,unit=unit)
            emit(response)
        }

    }
    suspend fun getForecastByCoord(lat: Double = this.lat,
                                         lon: Double=this.lon,
                                         lang: String = this.lang,
                                         unit: String = this.unit): Flow<ForecastData> {
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