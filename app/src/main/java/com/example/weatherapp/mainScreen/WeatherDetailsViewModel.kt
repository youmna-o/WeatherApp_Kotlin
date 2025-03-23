package com.example.weatherapp.mainScreen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.remote.Response
import com.example.weatherapp.data.repo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherDetailsViewModel(private val repo: Repo): ViewModel() {


    private val currentWeather: MutableStateFlow<Response<WeatherData>> = MutableStateFlow(Response.Loading())
    val weather: StateFlow<Response<WeatherData>> = currentWeather.asStateFlow()
    private val mutableForecast: MutableStateFlow<Response<ForecastData>> = MutableStateFlow(Response.Loading())
    val forecast: StateFlow<Response<ForecastData>> = mutableForecast.asStateFlow()

    private val mutableMessage: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> =mutableMessage

    fun getCurrentWeather(){
        viewModelScope.launch ( Dispatchers.IO){

                repo.getCurrentWeather(true) .catch { ex ->
                    currentWeather.value = Response.Failure(ex)
                }
                    .collect { list ->
                        currentWeather.value = Response.Success(list)
                    }

                }

        }


    fun getForecast(){
        viewModelScope.launch ( Dispatchers.IO){
            repo.getForecast(true)
                .catch { ex ->
                    mutableForecast.value = Response.Failure(ex)
                }
                .collect { list ->
                    mutableForecast.value = Response.Success(list)
                }

        }
    }


    override fun onCleared() { //when my view die
        super.onCleared()
    }

}


class myFactory(private val repo: Repo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherDetailsViewModel(repo) as T

    }
}