package com.example.weatherapp.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.data.Response
import com.example.weatherapp.data.model.FavCity


@Composable
fun FavouritScreen(favViewModel: FavViewModel){
  favViewModel.addFavCity(FavCity("mm",0.23,3.5))
    getAllFavCities(favViewModel)
}
@Composable
private fun getAllFavCities(favViewModel: FavViewModel) {
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
                Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(cities) { _, currentCity ->
                    CityCard(currentCity.name)
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
fun CityCard(label: String) {
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
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = label,
                fontSize = 28.sp,
            )

        }

    }
}