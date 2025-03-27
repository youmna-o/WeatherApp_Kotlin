package com.example.weatherapp.weatherScreen

import android.app.Application
import android.content.Context
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

class WeatherDetailsViewModel(private val repo: Repo,application: Application): ViewModel() {
    private val sharedPreferences = application.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    private val currentWeather: MutableStateFlow<Response<WeatherData>> = MutableStateFlow(Response.Loading())
    val weather: StateFlow<Response<WeatherData>> = currentWeather.asStateFlow()
    private val mutableForecast: MutableStateFlow<Response<ForecastData>> = MutableStateFlow(
        Response.Loading())
    val forecast: StateFlow<Response<ForecastData>> = mutableForecast.asStateFlow()
    private val mutableMessage: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> =mutableMessage

    private val defLang=MutableStateFlow(sharedPreferences.getString("lang","en")?:"en")
    val lang : StateFlow<String> = defLang.asStateFlow()
    private val defCity=MutableStateFlow(sharedPreferences.getString("city","cairo")?:"cairo")
    val city : StateFlow<String> = defCity.asStateFlow()
    private val defWind=MutableStateFlow(sharedPreferences.getString("wind","meter/sec")?:"meter/sec")
    val wind : StateFlow<String> = defWind.asStateFlow()
    private val defTemp=MutableStateFlow(sharedPreferences.getString("temp","Celsius")?:"Celsius")
    val temp : StateFlow<String> = defTemp.asStateFlow()
    private val defUnit=MutableStateFlow(sharedPreferences.getString("unit","metric")?:"metric")
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
        var newUnit= defUnit.value
        sharedPreferences.edit().putString("unit",newUnit).apply()

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


class myFactory(private val repo: Repo,private val application: Application): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherDetailsViewModel(repo, application = application) as T

    }
}