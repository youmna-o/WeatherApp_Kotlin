package com.example.weatherapp.favorite

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.weatherapp.R
import com.example.weatherapp.data.Response
import com.example.weatherapp.data.model.FavCity
import com.example.weatherapp.ui.theme.myOrange
import com.example.weatherapp.ui.theme.myPurple
import com.example.weatherapp.weatherScreen.WeatherDetailsScreen
import com.example.weatherapp.weatherScreen.WeatherDetailsViewModel


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.style.TextAlign
import com.example.weatherapp.utils.NetworkUtils.isNetworkAvailable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavouritScreen(
    favViewModel: FavViewModel,
    viewModel: WeatherDetailsViewModel,
    navController: NavController
) {
    var selectedCity by remember { mutableStateOf<FavCity?>(null) }

    if (selectedCity != null) {
        BackHandler {
            selectedCity = null
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        selectedCity?.let { city ->
            WeatherDetailsScreen(
                viewModel = viewModel,
                currentLat = city.lat,
                currentLon = city.lon
            )
        }

        getAllFavCities(
            favViewModel = favViewModel,
            viewModel = viewModel,
            navController = navController
        ) { clickedCity ->
            selectedCity = clickedCity
            viewModel.updateMapLocation(clickedCity.lat, clickedCity.lon)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun getAllFavCities(
    favViewModel: FavViewModel,
    viewModel: WeatherDetailsViewModel,
    navController: NavController,
    onCityClicked: (FavCity) -> Unit
) {
    val favCityState by favViewModel.FavCiyt.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        favViewModel.getFavCities()
    }

    when (favCityState) {
        is Response.Loading<*> -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Response.Success<*> -> {
            val cities = (favCityState as Response.Success<List<FavCity>>).data
            Column {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(all = 16.dp)
                        .clickable() {
                            navController.navigate("map")
                        }
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.3f)
                    )){
                    Text(text = stringResource(R.string.add_to_favorites),
                        maxLines = 1, fontSize = 24.sp,textAlign= TextAlign.Center, modifier = Modifier.padding(start = 8.dp, top = 8.dp).fillMaxWidth())

                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(cities) { _, currentCity ->
                        CityCard(
                            city = currentCity,
                            viewModel = viewModel,
                            navController = navController,
                            favViewModel = favViewModel,
                            onClick = { onCityClicked(currentCity) }
                        )
                    }
                }
            }

        }

        is Response.Failure<*> -> {
            val errorMessage = (favCityState as Response.Failure<*>).error.message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: $errorMessage", color = Color.Red)
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CityCard(
    city: FavCity,
    viewModel: WeatherDetailsViewModel,
    navController: NavController,
    favViewModel: FavViewModel,
    onClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = city.name,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = myPurple,
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(R.string.remove))
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            containerColor = myPurple,
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.delete)) },
            text = { Text(stringResource(R.string.do_you_want_to_remove_this_city_from_favorites)) },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        favViewModel.deleteFavCity(city)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myOrange,
                        contentColor = Color.White
                    )
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

