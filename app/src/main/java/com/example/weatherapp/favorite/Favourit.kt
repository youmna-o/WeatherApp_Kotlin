package com.example.weatherapp.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.weatherapp.data.Response
import com.example.weatherapp.data.model.FavCity
import com.example.weatherapp.ui.theme.myBlue
import com.example.weatherapp.ui.theme.myOrange
import com.example.weatherapp.ui.theme.myPurple
import com.example.weatherapp.weatherScreen.WeatherDetailsViewModel


@Composable
fun FavouritScreen(favViewModel: FavViewModel,viewModel: WeatherDetailsViewModel,navController: NavController){

    favViewModel.addFavCity(FavCity("mm",0.23,3.5))
    getAllFavCities(favViewModel,viewModel,navController)
}
@Composable
private fun getAllFavCities(favViewModel: FavViewModel,viewModel: WeatherDetailsViewModel,navController: NavController) {
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
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(cities) { _, currentCity ->
                    CityCard( currentCity, viewModel,navController,favViewModel )
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


@Composable
fun CityCard(city: FavCity , viewModel: WeatherDetailsViewModel,navController: NavController,favViewModel: FavViewModel ) {
    var clicked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.3f)
        ),

        ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    viewModel.updateMapLocation(city.lat, city.lon)
                    navController.navigate("weather_screen/${city.lat}/${city.lon}")
                },
        ) {
            Text(
                text = city.name,
                fontSize = 28.sp,
            )
            Spacer(Modifier.weight(1f))
            Button({
                clicked=true
            },colors =  ButtonDefaults.buttonColors(
                containerColor = myPurple,
                contentColor = Color.White
            )) {
                Text("remove")
            }

        }


    }
    if (clicked) {
        AlertDialog(
            containerColor = myPurple,
            onDismissRequest = { clicked= false },
            title = { Text("Delete") },
            text = { Text("Do you want to remove this city from favorites") },
            confirmButton = {
                Button(onClick = { clicked = false
                    favViewModel.deleteFavCity(city) },
                    colors =  ButtonDefaults.buttonColors(
                        containerColor = myOrange,
                        contentColor = Color.White
                    )
                ){
                    Text("Ok")

                }

            },
            dismissButton = {
                Button(onClick = { clicked = false
                }, ) {
                    Text("Cancel")
                }
            }


        )
    }
}
