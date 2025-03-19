package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.data.remote.RetrofitHeloer
import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.repo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var currentWeather: MutableState<WeatherData>
    private lateinit var foreCast: MutableState<ForecastData>
    private lateinit var repo: Repo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val remoteDataSource = WeatherRemoteDataSource(RetrofitHeloer.apiService)
        repo = Repo.getInstance(remoteDataSource)
        setContent {
            currentWeather = remember { mutableStateOf(WeatherData()) }
            foreCast = remember { mutableStateOf(ForecastData()) }


        }
        lifecycleScope.launch(Dispatchers.IO) {
                val weather = repo.getCurrentWeather().main
            Log.i("TAG2", "onCreate: ${weather}")
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val forecast = repo.getForecast().list[5]
            Log.i("TAG", "onCreate: ${forecast}")
        }

        }
    }
