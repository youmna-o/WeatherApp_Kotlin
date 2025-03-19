package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.data.repo.RetrofitHeloer
import com.example.weatherapp.ui.ForecastData
import com.example.weatherapp.ui.WeatherData
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var currentWeather: MutableState<WeatherData>
    private lateinit var foreCast: MutableState<ForecastData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            currentWeather = remember { mutableStateOf(WeatherData()) }
            foreCast = remember { mutableStateOf(ForecastData()) }


        }
        lifecycleScope.launch (Dispatchers.IO){
            val weatherServices= RetrofitHeloer.apiService
            val response = weatherServices.getWeather("cairo","e13a916deb17bb538fdfabaf0d57e6a5")
            val weather=response.main
            Log.i("TAG", "onCreate: ${weather}")


            }
        lifecycleScope.launch (Dispatchers.IO){
            val weatherServices= RetrofitHeloer.apiService
            val response = weatherServices.getForecast("cairo","e13a916deb17bb538fdfabaf0d57e6a5")
            val forecast=response.list[5]
            Log.i("TAG", "onCreate: ${forecast}")


        }
        }
    }
