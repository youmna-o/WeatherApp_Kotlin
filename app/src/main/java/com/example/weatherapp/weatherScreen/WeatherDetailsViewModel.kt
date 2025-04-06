package com.example.weatherapp.weatherScreen

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.Response
import com.example.weatherapp.data.repo.Repo
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Locale

class WeatherDetailsViewModel(private val repo: Repo,application: Application): ViewModel() {
    private val sharedPreferences = application.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val currentWeather: MutableStateFlow<Response<WeatherData>> = MutableStateFlow(Response.Loading())
    val weather: StateFlow<Response<WeatherData>> = currentWeather.asStateFlow()
    private val mutableForecast: MutableStateFlow<Response<ForecastData>> = MutableStateFlow(
        Response.Loading())
    val forecast: StateFlow<Response<ForecastData>> = mutableForecast.asStateFlow()
   //show data of dikirnis hard code till currentlocation load
   private val defLat=MutableStateFlow(sharedPreferences.getString("lat","31.0797867")?.toDouble()?:31.594271)
    val lat : StateFlow<Double> = defLat.asStateFlow()
    private val defLon=MutableStateFlow(sharedPreferences.getString("lon","31.590905")?.toDouble()?:31.590905)
    val lon: StateFlow<Double> = defLon.asStateFlow()
   // data of map
    private val _userLocation = mutableStateOf<LatLng?>(null)
    val userLocation: State<LatLng?> = _userLocation
    private val defMapLat=MutableStateFlow<Double>(0.0)
    private val defMapLon=MutableStateFlow<Double>(0.0)
    //data of setting options:
    private val defLocationMethod=MutableStateFlow(sharedPreferences.getString("locationMethod","GPS")?:"GPS")
    val locationMethod : StateFlow<String> = defLocationMethod.asStateFlow()
    private val defLang=MutableStateFlow(sharedPreferences.getString("lang","en")?:"en")
    val lang : StateFlow<String> = defLang.asStateFlow()
    private val defWind=MutableStateFlow(sharedPreferences.getString("wind","meter/sec")?:"meter/sec")
    val wind : StateFlow<String> = defWind.asStateFlow()
    private val defTemp=MutableStateFlow(sharedPreferences.getString("temp","Celsius")?:"Celsius")
    val temp : StateFlow<String> = defTemp.asStateFlow()
    private val defUnit=MutableStateFlow(sharedPreferences.getString("unit","metric")?:"metric")
    val unit : StateFlow<String> = defUnit.asStateFlow()


    fun selectLocation(selectedPlace: String, context: Context) {
        viewModelScope.launch {
            val geocoder = Geocoder(context)
            val addresses = withContext(Dispatchers.IO) {
                // Perform geocoding on a background thread
                geocoder.getFromLocationName(selectedPlace, 1)
            }
            if (!addresses.isNullOrEmpty()) {
                // Update the selected location in the state
                val address = addresses[0]
                val latLng = LatLng(address.latitude, address.longitude)
                _userLocation.value = latLng
            } else {
                Timber.tag("MapScreen").e("No location found for the selected place.")
            }
        }
    }
    fun setAppLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun setUserLocation(latLng: LatLng) {
        _userLocation.value = latLng
    }

   fun updateCurrentLocation(newLat:Double,newLon:Double){
       Log.i("TAGE", "updateCurrentLocation: ${newLat}")
       defLat.update { newLat }
       defLon.update { newLon }
       sharedPreferences.edit()
           .putString("lat", newLat.toString())
           .putString("lon", newLon.toString())
           .apply()
       Log.i("TAGE", "updateCurrentLocation: ${newLat}")
   }
    fun updateMapLocation(newLat:Double,newLon:Double){
        Log.i("TAGE", "updateCurrentLocation: ${newLat}")
        defMapLat.update { newLat }
        defMapLon.update { newLon }
        sharedPreferences.edit()
            .putString("mapLat", newLat.toString())
            .putString("mapLon", newLon.toString())
            .apply()
        Log.i("TAGE", "updateCurrentLocation: ${newLat}")
    }

    fun updateParameters(newLocationMethod:String,newLang:String, newTemp: String, newWind: String){
        defLocationMethod.value =newLocationMethod
        //sharedPreferences.edit().putString("locationMethod",newLocationMethod).apply()
        defLang.value=newLang
        defWind.value=newWind
        defTemp.value=newTemp
        defUnit.value = when {
            newTemp == "Fahrenheit" && newWind == "mile/h" -> "imperial"
            newTemp == "Celsius" && newWind == "meter/sec" -> "metric"
            newTemp == "Kelvin" -> ""
            else -> "metric"
        }
        var newUnit= defUnit.value
        sharedPreferences.edit().putString("unit",newUnit).apply()

    }

    fun getCurrentWeather(city:String,lang:String,unit:String){
        viewModelScope.launch ( Dispatchers.IO){
                repo.getCurrentWeather(city=city,lang=lang,unit=unit) .catch { ex ->
                    currentWeather.value = Response.Failure(ex)
                }
                    .collect { list ->
                        currentWeather.value = Response.Success(list)
                    }
                }
        }
    fun getCurrentWeatherByCoord(lat:Double,lon:Double,lang:String,unit:String){
        viewModelScope.launch ( Dispatchers.IO){
            repo.getCurrentWeatherByCoord(true,lat=lat,lon=lon,lang=lang,unit=unit) .catch { ex ->
                currentWeather.value = Response.Failure(ex)
            }
                .collect { list ->
                    currentWeather.value = Response.Success(list)
                }
        }
    }

    fun getForecast(city:String,lang:String,unit:String){
        viewModelScope.launch ( Dispatchers.IO){
            repo.getForecast(true,city=city,lang=lang,unit=unit)
                .catch { ex ->
                    mutableForecast.value = Response.Failure(ex)
                }
                .collect { list ->
                    mutableForecast.value = Response.Success(list)
                }
        }
    }
    fun getForecastByCoord(lat:Double,lon:Double,lang:String,unit:String){
        viewModelScope.launch ( Dispatchers.IO){
            repo.getForecastByCoord(true,lat=lat,lon = lon,lang=lang,unit=unit)
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