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
import com.example.weatherapp.data.repo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherDetailsViewModel(private val repo: Repo): ViewModel() {
    private val currentWeather: MutableLiveData<WeatherData> = MutableLiveData()
    val weather: LiveData<WeatherData> = currentWeather
    private val mutableForecast: MutableLiveData<ForecastData> = MutableLiveData()
    val forecast: LiveData<ForecastData> = mutableForecast

    private val mutableMessage: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> =mutableMessage

    fun getCurrentWeather(){
        viewModelScope.launch ( Dispatchers.IO){
            try {
                val  result = repo.getCurrentWeather(true)
                if (result!=null){
                    val myWeather = result
                    currentWeather.postValue(myWeather)
                   // Log.i("TAG2", "onCreate: ${weather.value?.weather?.get(0)}")
                }else{
                    mutableMessage.postValue("faild")
                }

            }catch (ex:Exception){
                mutableMessage.postValue("An erroe ,${ex.message}")
            }

        }
    }
    fun getForecast(){
        viewModelScope.launch ( Dispatchers.IO){
            try {
                val  result = repo.getForecast(true)
               // val forecastList = forecastResponse.list // تحتوي على 40 قيمة

                if (result!=null){
                    val myForecast = result
                    mutableForecast.postValue(myForecast)

                //    Log.i("TAG2", "onCreate: ${weather.value?.weather?.get(0)}")
                }else{
                    mutableMessage.postValue("faild")
                }

            }catch (ex:Exception){
                mutableMessage.postValue("An erroe ,${ex.message}")
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