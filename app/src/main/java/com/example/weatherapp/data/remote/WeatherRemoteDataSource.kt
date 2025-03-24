package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSource(private  val service: WeatherServices,
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

       // return service.getWeather("cairo","e13a916deb17bb538fdfabaf0d57e6a5","ar")
    }
    suspend fun getForecast(): Flow<ForecastData> {
        return flow {
            val response = service.getForecast("cairo","e13a916deb17bb538fdfabaf0d57e6a5")
            emit(response)
        }
    }
}