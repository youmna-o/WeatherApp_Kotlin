package com.example.weatherapp.map

import MapSearchBar
import MapViewModel
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.navigation.NavController
import com.example.weatherapp.R
import com.example.weatherapp.data.model.FavCity
import com.example.weatherapp.favorite.FavViewModel
import com.example.weatherapp.ui.theme.myBlue
import com.example.weatherapp.ui.theme.myOrange
import com.example.weatherapp.ui.theme.myPurple
import com.example.weatherapp.utils.NetworkUtils.isNetworkAvailable
import com.example.weatherapp.weatherScreen.WeatherDetailsViewModel
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import timber.log.Timber
import java.util.Locale

@Composable
fun MapScreen(viewModel: WeatherDetailsViewModel,navController: NavController,favViewModel: FavViewModel,mapViewModel: MapViewModel) {
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    var lat :Double=0.0
    var lon :Double=0.0
    val userLocation by viewModel.userLocation
    var addressState = remember { mutableStateOf("address") }
    var city :String="address"
    var showDialog by remember { mutableStateOf(false) }
    val userMapLocation by mapViewModel.userLocation
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            mapViewModel.fetchUserLocation(context, fusedLocationClient)
        } else {
            Timber.e("Location permission was denied by the user.")
        }
    }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
                mapViewModel.fetchUserLocation(context, fusedLocationClient)
            }
            else -> {
                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
    if (isNetworkAvailable(context)){
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
                            snippet = "This is where you are currently located"
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
                            editor.putString("locationMethod", "Map").apply()
                            lat = viewModel.userLocation.value?.latitude ?: 0.0
                            lon = viewModel.userLocation.value?.longitude ?: 0.0
                            viewModel.updateMapLocation(lat, lon)
                            navController.navigate("weather_screen/$lat/$lon")
                        }
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = myBlue
                    )){
                    Text(
                        stringResource(R.string.get_weather), maxLines = 1, fontSize = 20.sp,textAlign= TextAlign.Center, modifier = Modifier
                            .padding(top = 8.dp)
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
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.added_to_favorites), Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = myBlue
                    )){
                    Text(
                        stringResource(R.string.add_to_favorites), maxLines = 1, fontSize = 20.sp,textAlign= TextAlign.Center, modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth())

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
                    title = { Text(stringResource(R.string.warning)) },
                    text = { Text(stringResource(R.string.location_not_found_please_select_a_valid_location)) },
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
    }else{
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Wait to connect the Internet")
            CircularProgressIndicator()
        }

    }


    }


