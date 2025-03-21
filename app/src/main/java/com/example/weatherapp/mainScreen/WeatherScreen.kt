package com.example.weatherapp.mainScreen


import android.media.audiofx.AudioEffect.Descriptor
import android.os.Build

import android.util.Log

import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.model.forecastList
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherScreen(weatherData: WeatherData,forecastData: List<forecastList>) {
    val showBottomSheet = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.backani))

        LottieAnimation(
            composition = composition,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            ToDayWeatherCard("${weatherData.name}", { BigIcon("${weatherData.weather[0].icon}") },
                "${getCurrentDateTime()}", temp =TempFromKToC(weatherData.main?.temp), description ="${weatherData.weather[0].description}" )
            Spacer(modifier = Modifier.height(16.dp))
            ToDayDetailsCard(weatherData.main?.pressure, weatherData.main?.humidity, allClouds = weatherData.clouds?.all, speed = weatherData.wind?.speed )

            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(forecastData.take(8), key = { it.dtTxt!! }) { item ->
                    Log.d("ForecastLazyRow", "Time: ${item.dtTxt}, Weather: ${item.weather[0].description}")
                    RestOfDay(
                        hour = "${item.dtTxt}",
                        icon = { MediumIcon("${item.weather[0].icon}") },
                        temp = TempFromKToC(item.main?.temp),

                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clickable() {
                            showBottomSheet.value = true
                        }
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.3f)
                    )){
                    Text(text = "Show The Next Days Forecast", fontSize = 24.sp,textAlign=TextAlign.Center, modifier = Modifier.padding(start = 8.dp, top = 8.dp))

            }

        }
        if (showBottomSheet.value) {
            PartialBottomSheet(showBottomSheet,forecastData)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ToDayWeatherCard( city: String,icon :@Composable  ()-> Unit, date:String,temp:Int,description:String,) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            //.width(340.dp)
            .height(280.dp)
            .padding(top = 32.dp, bottom = 16.dp, start = 40.dp, end = 40.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.3f)
        ),

        ) {
        Row ( modifier = Modifier.padding(16.dp).padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            ){
            Column {
                Text(
                    text = city,
                    fontSize = 28.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(20.dp))
                icon()
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column (horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                Text(
                    text = getDayDate(date) ,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = temp.toString()+ "C",
                    fontSize = 40.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = description,
                    fontSize = 20.sp,
                    color = Color.Black
                )

            }

        }

    }
}
@Composable
fun ToDayDetailsCard(pressure  : Int?, humidity  : Int?,allClouds : Int? ,speed:Double? ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.3f)
        ),

        ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text ="pressure"+ pressure.toString()+"hPa",
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "humidity"+humidity.toString()+"%",
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text ="allClouds"+ allClouds.toString()+"%",
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "speed"+speed.toString()+"m/s",
                fontSize = 16.sp,
                color = Color.Black
            )

        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RestOfDay(hour : String, icon:@Composable () -> Unit,temp:Int) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(200.dp)
            .padding(vertical = 16.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.3f)
        ),

        ) {
        Column (
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = " at "+getHourFromDateTime(hour)+":00",
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Spacer(modifier = Modifier.weight(1f))
            icon()
            Spacer(modifier = Modifier.width(8.dp))
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${temp} C",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }

}

@Composable
fun WeatherCard(label: String, value: Int?, description: String,icon:@Composable () -> Unit) {
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
            icon()
            Text(
                text = label,
                fontSize = 28.sp,
            )

            Spacer(modifier = Modifier.weight(1f))
           Row {  Text(
               text = value.toString() + "C",
               fontSize = 24.sp,
               fontWeight = FontWeight.Bold,
           )
               Text(
                   text = "/"+description,
                   fontSize = 24.sp,
                   fontWeight = FontWeight.Bold,
               ) }


        }

    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NextDaysWeather(forecastList: List<forecastList>) {
    Column {
        Text(
            text = "Forecast For Next Days",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        LazyColumn {
            items(listOf(8,16,24,32).mapNotNull {
                    index -> forecastList.getOrNull(index)
            }) { weather ->
                WeatherCard(
                    label = getDayName("${weather.dtTxt}" ),
                    value = TempFromKToC(weather.main?.temp),
                    description = "${weather.weather[0].description}",
                    icon = {WeatherIcon("${weather.weather[0].icon}") }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartialBottomSheet(showBottomSheet: MutableState<Boolean>, forecastList: List<forecastList>) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    if (showBottomSheet.value) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet.value = false }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.backani))
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds
                )

                NextDaysWeather(forecastList)
            }
        }
    }
}