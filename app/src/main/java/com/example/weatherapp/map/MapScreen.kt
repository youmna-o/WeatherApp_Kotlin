package com.example.weatherapp.map

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import timber.log.Timber

@Composable
fun MapScreen(mapViewModel: MapViewModel) {
    val context = LocalContext.current
    val userLocation by mapViewModel.userLocation
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

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
                onMapClick = { latLng ->
            mapViewModel.setUserLocation(latLng)
        }
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
