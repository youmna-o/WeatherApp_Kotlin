package com.example.weatherapp
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.repo.Repo
import com.example.weatherapp.mainActivity.ShowNavBar
import com.example.weatherapp.map.MapViewModel
import com.example.weatherapp.notifications.NotificationAlarmScheduler
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utils.ManifestUtils
import com.example.weatherapp.weatherScreen.WeatherDetailsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

const val REQUEST_LOCATION_CODE = 2005
class MainActivity : ComponentActivity() {
    private val notificationAlarmScheduler by lazy {
        NotificationAlarmScheduler(this)
    }

    private val weatherViewModel: WeatherDetailsViewModel by viewModels()
 //   val viewModel=WeatherDetailsViewModel()
    //lateinit var addressState: MutableState<String>
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationState:MutableState<Location>
   // var sharedPreferences=this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val apiKey = ManifestUtils.getApiKeyFromManifest(this)
       // Initialize the Places API with the retrieved API key
       if (!Places.isInitialized() && apiKey != null) {
           Places.initialize(applicationContext, apiKey)
       }
        setContent {
     WeatherAppTheme {
       //  Log.i("k", "onCreate: ${sharedPreferences.getString("lat","31.0797867")}")
         val currentContext = LocalContext.current
         locationState= remember { mutableStateOf(Location(LocationManager.GPS_PROVIDER).apply {
             latitude = 31.0797867
             longitude = 31.590905
         }) }
         var lat=locationState.value.latitude
         var lon = locationState.value.longitude
         Log.e("TestLog", "This is a test log message!${locationState.value.latitude }++++++++++ ${locationState.value.longitude} ")
        // addressState = remember { mutableStateOf("") }

         ShowNavBar(this, application = application,lat,lon,notificationAlarmScheduler)

     }
            /*  Box(
           modifier = Modifier
               .fillMaxSize()
       ) {
           //Image(painter = painterResource(R.drawable.background), contentDescription = "nn",Modifier.fillMaxWidth())
           val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash2  ))
           val progress by animateLottieCompositionAsState(
               composition,
               iterations = LottieConstants.IterateForever
           )

           LottieAnimation(
               composition = composition,
               progress = { progress },
               modifier = Modifier.fillMaxSize(),
               contentScale = ContentScale.FillBounds
           )

       }*/

     }


    }
    override fun onStart() {
        super.onStart()
        if(checkPermission()){
            if(isLocationEnabled()){
                getFreshLocation()
            }else{
                enableLocationSetting()
            }
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_CODE
            )

        }
    }
    //to handle every permission which accepted and which not
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        //may have more than one permission
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if(requestCode==REQUEST_LOCATION_CODE){
            if(grantResults.get(0)== PackageManager.PERMISSION_GRANTED|| grantResults.get(1)== PackageManager.PERMISSION_GRANTED){
                if(isLocationEnabled()){
                    getFreshLocation()
                }else{
                    enableLocationSetting()
                }
            }
        }
    }
    //must run time permission
    fun checkPermission(): Boolean{
        var result = false
        if ((ContextCompat.checkSelfPermission(this,
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
            ||
            (ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION
            ) ==  PackageManager.PERMISSION_GRANTED))
        {
            result  = true
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_CODE
            )
        }
        return result
    }

    @SuppressLint("MissingPermission")
    fun  getFreshLocation(){
        //fuse is my entry point to return the location
        //listener to update location
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this@MainActivity)
        fusedLocationProviderClient.requestLocationUpdates(
            //take request and call back and lopper
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build(),
            object : LocationCallback(){
                override fun onLocationResult(location: LocationResult) {
                    super.onLocationResult(location)
                    val myLocation =location.lastLocation?: Location(LocationManager.GPS_PROVIDER)
                    locationState.value=myLocation
                   // weatherViewModel.updateCurrentLocation(myLocation.latitude,myLocation.longitude)
                    //getAddress(myLocation)
                }
            },
            Looper.myLooper()
        )
    }


    fun enableLocationSetting(){//if user close location
        Toast.makeText(this,"turn on location", Toast.LENGTH_LONG).show()
        val intent= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }
    private fun isLocationEnabled():Boolean{ //if user open or close location
        val locationManager: LocationManager =getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(object : LocationCallback() {})
    }


}


