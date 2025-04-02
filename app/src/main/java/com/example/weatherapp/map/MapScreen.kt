package com.example.weatherapp.map

import MapSearchBar
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.weatherapp.R
import com.example.weatherapp.data.model.FavCity
import com.example.weatherapp.favorite.FavViewModel
import com.example.weatherapp.settings.RadioButtonSingleSelection
import com.example.weatherapp.ui.theme.myBlue
import com.example.weatherapp.ui.theme.myOrange
import com.example.weatherapp.ui.theme.myPurple
import com.example.weatherapp.weatherScreen.WeatherDetailsViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Locale

@Composable
fun MapScreen(viewModel: WeatherDetailsViewModel,navController: NavController,favViewModel: FavViewModel) {
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    var lat :Double=0.0
    var lon :Double=0.0
    val userLocation by viewModel.userLocation
    var geo =Geocoder(context, Locale.getDefault())
    var addressState = remember { mutableStateOf("address") }
    var city :String="address"
    var showDialog by remember { mutableStateOf(false) }





    Column (modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        MapSearchBar (
            onPlaceSelected = { place ->
                viewModel.selectLocation(place, context)
            }
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(540.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = myBlue
            ),
        ) {
             GoogleMap(
                   modifier = Modifier.fillMaxSize(),
                           onMapClick = { latLng ->
                          viewModel.setUserLocation(latLng) }
               ){
                   userLocation?.let {
                       Marker(
                           state = MarkerState(position = it),
                           title = "Your Location",
                           snippet = "This is where you are currently located."
                       )
                   }
               }
            }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
                    .clickable() {
                        editor.putString("locationMethod","Map").apply()
                        lat = viewModel.userLocation.value?.latitude ?: 0.0
                        lon = viewModel.userLocation.value?.longitude ?: 0.0
                        viewModel.updateMapLocation(lat,lon)
                        navController.navigate("weather_screen/$lat/$lon")
                    }
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = myBlue
                )){
                Text("Get Weather", maxLines = 1, fontSize = 20.sp,textAlign= TextAlign.Center, modifier = Modifier
                    .padding( top = 8.dp)
                    .fillMaxSize())

            }
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
                    .clickable() {
                        lat = viewModel.userLocation.value?.latitude ?: 0.0
                        lon = viewModel.userLocation.value?.longitude ?: 0.0
                        if (addressState.value == "No country found") {
                            showDialog = true
                        } else {
                            favViewModel.addFavCity(FavCity(addressState.value, lat, lon))
                            Toast.makeText(context,"Added To Favorites",Toast.LENGTH_LONG).show()
                        }
                    }
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = myBlue
                )){
                Text("Add To Favorites", maxLines = 1, fontSize = 20.sp,textAlign= TextAlign.Center, modifier = Modifier.padding( top = 8.dp).fillMaxWidth())

            }
        }

        fun getAddress(context: Context ,lat:Double, lon:Double):String {
            val addresses = Geocoder(context, Locale.getDefault())
                .getFromLocation(lat, lon, 1)
              city = addresses?.firstOrNull()?.locality ?: "No country found"
            Log.e("Geocoder Error", "111${city}", )
            println(city)
            return city
        }
        LaunchedEffect(userLocation) {
            userLocation?.let {
                location ->
                addressState.value="loading"
                addressState.value= getAddress(context,location.latitude,location.longitude)
                Log.e("Geocoder Error", "final ${addressState.value}", )


            }

        }
        if (showDialog) {
            AlertDialog(
                containerColor = myPurple,
                onDismissRequest = { showDialog = false },
                title = { Text("Error") },
                text = { Text("Location not found. Please select a valid location.") },
                confirmButton = {
                    Button(onClick = { showDialog = false }, colors =  ButtonDefaults.buttonColors(
                        containerColor = myOrange,
                        contentColor = Color.White
                    )) {
                        Text("OK")
                    }
                }
            )
        }

                            }
    }


   /* val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            mapViewModel.fetchUserLocation(context, fusedLocationClient)
        } else {
            Timber.e("Location permission was denied by the user.")
        }
    }*/

// Request the location permission when the composable is launched
  /*  LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
                mapViewModel.fetchUserLocation(context, fusedLocationClient)
            }
            else -> {
                // Request the location permission if it has not been granted
                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }*/


/*      fun getAddress(lat:Double, lon:Double) {
           CoroutineScope(Dispatchers.IO).launch {
               try {
                   val addresses = Geocoder(context, Locale.getDefault())
                       .getFromLocation(lat, lon, 1)

                   val city = addresses?.firstOrNull()?.locality ?: "No country found"
                   Log.e("Geocoder Error", "111${city}", )
                   println(city)
                   withContext(Dispatchers.Main) {
                       addressState.value=city
                   }
               } catch (e: Exception) {
                   withContext(Dispatchers.Main) {
                       addressState.value = "Error getting address"
                   }
                   Log.e("Geocoder Error", "Failed to get address", e)
               }
           }
       }*/
// getAddress(lat,lon)
//Log.e("3Geocoder Error", "22222222${lat}${lon}", )

/*    lat = viewModel.userLocation.value?.latitude ?: 0.0
    lon = viewModel.userLocation.value?.longitude ?: 0.0

    viewModel.updateMapLocation(lat,lon)
*/
