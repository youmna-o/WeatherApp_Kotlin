package com.example.weatherapp.weatherScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.Response
import com.example.weatherapp.data.repo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WeatherDetailsViewModel(private val repo: Repo): ViewModel() {

    private val currentWeather: MutableStateFlow<Response<WeatherData>> = MutableStateFlow(Response.Loading())
    val weather: StateFlow<Response<WeatherData>> = currentWeather.asStateFlow()
    private val mutableForecast: MutableStateFlow<Response<ForecastData>> = MutableStateFlow(
        Response.Loading())
    val forecast: StateFlow<Response<ForecastData>> = mutableForecast.asStateFlow()
    private val mutableMessage: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> =mutableMessage

    private val defLang=MutableStateFlow("en")
    val lang : StateFlow<String> = defLang.asStateFlow()
    private val defCity=MutableStateFlow("cairo")
    val city : StateFlow<String> = defCity.asStateFlow()
    private val defWind=MutableStateFlow("meter/sec")
    val wind : StateFlow<String> = defWind.asStateFlow()
    private val defTemp=MutableStateFlow("Celsius")
    val temp : StateFlow<String> = defTemp.asStateFlow()
    private val defUnit=MutableStateFlow("metric")
    val unit : StateFlow<String> = defUnit.asStateFlow()

    fun updateParameters(newCity:String,newLang:String, newTemp: String, newWind: String){
        defCity.value=newCity
        defLang.value=newLang
        defWind.value=newWind
        defTemp.value=newTemp
        defUnit.value = when {
            newTemp == "Fahrenheit" && newWind == "mile/h" -> "imperial"
            newTemp == "Celsius" && newWind == "meter/sec" -> "metric"
            newTemp == "Kelvin" -> ""
            else -> "Celsius"
        }

    }
    fun getCurrentWeather(city:String,lang:String,unit:String){
        viewModelScope.launch ( Dispatchers.IO){
                repo.getCurrentWeather(true,city=city,lang=lang,unit=unit) .catch { ex ->
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


}


class myFactory(private val repo: Repo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherDetailsViewModel(repo) as T

    }
}