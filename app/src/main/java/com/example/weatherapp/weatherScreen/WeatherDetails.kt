package com.example.weatherapp.weatherScreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle

import androidx.activity.compose.setContent
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.Response
import com.example.weatherapp.ui.theme.WeatherAppTheme


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherDetailsScreen(viewModel: WeatherDetailsViewModel,currentLat:Double ,currentLon:Double){
    WeatherAppTheme {
        val context = LocalContext.current
        if (isNetworkAvailable(context)){
            getWeatherAndForecast(context,viewModel,currentLat,currentLon)
        } else{
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
               Text("Wait to connect the Internet")
                CircularProgressIndicator()
            }

    }
    }
}
@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun getWeatherAndForecast(context :Context,viewModel: WeatherDetailsViewModel,currentLat:Double ,currentLon:Double) {
     val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
     val weatherState by viewModel.weather.collectAsStateWithLifecycle()
     val foreCastState by viewModel.forecast.collectAsStateWithLifecycle()
     val langState by viewModel.lang.collectAsStateWithLifecycle()
     val unitState by viewModel.unit.collectAsStateWithLifecycle()

        LaunchedEffect(currentLat,currentLon) {
            if(sharedPreferences.getString("locationMethod","GPS")=="GPS"){
                viewModel.updateCurrentLocation(currentLat,currentLon)
                viewModel.getForecastByCoord(currentLat,currentLon,langState,unitState)
                viewModel.getCurrentWeatherByCoord(currentLat,currentLon,langState,unitState)
            }else{ //must come from map
                viewModel.updateMapLocation(currentLat,currentLon)
                viewModel.getForecastByCoord(currentLat,currentLon,langState,unitState)
                viewModel.getCurrentWeatherByCoord(currentLat,currentLon,langState,unitState)
            }

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

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    return activeNetwork?.isConnected == true
}