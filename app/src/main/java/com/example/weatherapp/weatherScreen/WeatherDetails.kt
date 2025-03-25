package com.example.weatherapp.weatherScreen

import android.os.Build
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.Response

import com.example.weatherapp.data.remote.RetrofitHeloer
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.repo.Repo
import com.example.weatherapp.ui.theme.WeatherAppTheme


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherDetailsScreen(activity: ComponentActivity,viewModel: WeatherDetailsViewModel){
    WeatherAppTheme {
        val context = LocalContext.current
     getWeatherAndForecast(viewModel)
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun getWeatherAndForecast(viewModel: WeatherDetailsViewModel) {
    val weatherState by viewModel.weather.collectAsStateWithLifecycle()
    val foreCastState by viewModel.forecast.collectAsStateWithLifecycle()
    val cityState by viewModel.city.collectAsStateWithLifecycle()
    val langState by viewModel.lang.collectAsStateWithLifecycle()
    val unitState by viewModel.unit.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCurrentWeather(cityState,langState,unitState)
        viewModel.getForecast()
    }
    when (weatherState) {
        is Response.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Response.Success -> {
            val weather = (weatherState as Response.Success<WeatherData>).data
            val forecast = (foreCastState as? Response.Success<ForecastData>)?.data

            WeatherScreen(weather, forecast?.list ?: emptyList())
        }
        is Response.Failure -> {
            val errorMessage = (weatherState as Response.Failure).error.message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: $errorMessage", color = Color.Red)
            }
        }

    }
}

