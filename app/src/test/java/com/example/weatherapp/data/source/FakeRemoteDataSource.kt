package com.example.weatherapp.data.source

import com.example.weatherapp.data.model.Clouds
import com.example.weatherapp.data.model.Coord
import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.Main
import com.example.weatherapp.data.model.Sys
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.model.Wind
import com.example.weatherapp.data.remote.IWeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteDataSource : IWeatherRemoteDataSource {
    override suspend fun getCurrentWeather(city: String, lang: String, unit: String): Flow<WeatherData> {
        return flow {
            emit(
                WeatherData(
                    coord = Coord(lon = 31.2497, lat = 30.0626),
                    weather = listOf(
                        Weather(id = 800, main = "Clear", description = "clear sky", icon = "01d")
                    ),
                    base = "stations",
                    main = Main(
                        temp = 299.57,
                        feelsLike = 299.57,
                        tempMin = 298.94,
                        tempMax = 299.57,
                        pressure = 1013,
                        humidity = 25,
                        seaLevel = 1013,
                        grndLevel = 1007
                    ),
                    visibility = 10000,
                    wind = Wind(speed = 3.6, deg = 300),
                    clouds = Clouds(all = 0),
                    dt = 1743688844,
                    sys = Sys(
                        pod = "",
                        country = "EG",
                        sunrise = 1743651709,
                        sunset = 1743696863
                    ),
                    timezone = 7200,
                    id = 360630,
                    name = "Cairo",
                    cod = 200
                )
            )
        }
    }

    override suspend fun getCurrentWeatherByCoord(
        lat: Double,
        lon: Double,
        lang: String,
        unit: String
    ): Flow<WeatherData> {
        TODO("Not yet implemented")
    }

    override suspend fun getForecast(city: String, lang: String, unit: String): Flow<ForecastData> {
        TODO("Not yet implemented")
    }

    override suspend fun getForecastByCoord(
        lat: Double,
        lon: Double,
        lang: String,
        unit: String
    ): Flow<ForecastData> {
        TODO("Not yet implemented")
    }
}