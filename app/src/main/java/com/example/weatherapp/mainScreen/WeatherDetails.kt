package com.example.weatherapp.mainScreen

import android.os.Build
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.ui.platform.LocalContext

import androidx.lifecycle.ViewModelProvider

import com.example.weatherapp.data.remote.RetrofitHeloer
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.repo.Repo

class WeatherDetails : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val factory = myFactory(
                Repo(
                    WeatherRemoteDataSource(RetrofitHeloer.apiService),
                )
            )
            val viewModel = ViewModelProvider(this, factory).get(WeatherDetailsViewModel::class.java)
           // getWeather(viewModel)
            getWeatherAndForecast(viewModel)

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun getWeatherAndForecast(viewModel: WeatherDetailsViewModel) {
    val weatherState = viewModel.weather.observeAsState()
    val foreCastState = viewModel.forecast.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentWeather()
        viewModel.getForecast()
    }

    if (weatherState.value != null && foreCastState.value != null) {
        WeatherScreen(weatherState.value!!, foreCastState.value!!.list)
    }
}

