package com.example.weatherapp
import MapViewModel

import android.annotation.SuppressLint
import android.content.Context

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import com.example.weatherapp.mainActivity.ShowNavBar
import com.example.weatherapp.notifications.NotificationAlarmScheduler
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utils.LocationUtils.checkedPermissions
import com.example.weatherapp.utils.LocationUtils.enableLocationServices
import com.example.weatherapp.utils.LocationUtils.isLocationEnabled
import com.example.weatherapp.utils.ManifestUtils
import com.example.weatherapp.weatherScreen.WeatherDetailsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.libraries.places.api.Places
import java.util.Locale

const val REQUEST_LOCATION_CODE = 2005
var permissionRequestCount = 0
class MainActivity : ComponentActivity() {
   private val notificationAlarmScheduler by lazy {
        NotificationAlarmScheduler(this)
    }
    private lateinit var sharedPreferences: SharedPreferences
    private val mapViewModel: MapViewModel by viewModels()
    private val weatherViewModel: WeatherDetailsViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationState:MutableState<Location>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       var lat:Double=0.0
       var lon:Double=0.0
       var destination = intent?.getStringExtra("DESTINATION")
       val latFromIntent = intent?.getDoubleExtra("LAT", 31.0797867)?:31.0797867
       val lonFromIntent = intent?.getDoubleExtra("LON", 31.590905)?:31.590905
       val currentLanguage = Locale.getDefault().language
       sharedPreferences = application.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
       if(currentLanguage=="ar"){
           sharedPreferences.edit().putString("lang","ar").apply()
       }
       val savedLanguage = sharedPreferences.getString("lang", "en") ?: "en"
       fun setAppLocale(context: Context, languageCode: String) {
           val locale = Locale(languageCode)
           Locale.setDefault(locale)
           val config = Configuration()
           config.setLocale(locale)
           context.resources.updateConfiguration(config, context.resources.displayMetrics)
       }
       setAppLocale(this,savedLanguage)
       println(currentLanguage)
       Log.i("TAG111", "onCreate: ${currentLanguage}")
       val apiKey = ManifestUtils.getApiKeyFromManifest(this)
       // Initialize the Places API with the retrieved API key
       if (!Places.isInitialized() && apiKey != null) {
           Places.initialize(applicationContext, apiKey)
       }

        setContent {
     WeatherAppTheme {
         locationState= remember { mutableStateOf(Location(LocationManager.GPS_PROVIDER).apply {
             latitude = 31.0797867
             longitude = 31.590905
         }) }
         if(destination!=null){
             lat=latFromIntent
             lon=lonFromIntent
             destination=null
         }else{
          lat=locationState.value.latitude
          lon = locationState.value.longitude}
         ShowNavBar(this, application = application,lat,lon,notificationAlarmScheduler, mapViewModel = mapViewModel) }
        }
    }
    override fun onStart() {
        super.onStart()
        if(checkedPermissions(this)) {
            if(isLocationEnabled(this)){
                getFreshLocation()
            }else{
                enableLocationServices(this)
            }
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                    , android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_CODE
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build(),
            object : LocationCallback(){
                override fun onLocationResult(location: LocationResult) {
                    super.onLocationResult(location)

                    locationState.value = location.lastLocation?: Location(LocationManager.GPS_PROVIDER)
                }
            },
            Looper.myLooper()

        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if(requestCode == REQUEST_LOCATION_CODE){
            if(grantResults.get(0) == PackageManager.PERMISSION_GRANTED
                || grantResults.get(1) == PackageManager.PERMISSION_GRANTED){

                if(isLocationEnabled(this)){
                    getFreshLocation()
                }else{
                    enableLocationServices(this)
                }
            }else{
                //need update >> if user refused permissions
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                        , android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    REQUEST_LOCATION_CODE
                )
            }
        }
    }

}


