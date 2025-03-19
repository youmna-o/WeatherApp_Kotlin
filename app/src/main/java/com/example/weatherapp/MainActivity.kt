package com.example.weatherapp

import android.os.Bundle
import android.telecom.Call.Details
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherapp.data.remote.RetrofitHeloer
import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.repo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var currentWeather: MutableState<WeatherData>
    private lateinit var foreCast: MutableState<ForecastData>
    private lateinit var repo: Repo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  val remoteDataSource = WeatherRemoteDataSource(RetrofitHeloer.apiService)
        // repo = Repo.getInstance(remoteDataSource)
        setContent {
            currentWeather = remember { mutableStateOf(WeatherData()) }
            foreCast = remember { mutableStateOf(ForecastData()) }
            WeatherScreen()

            // PartialBottomSheet()


        }
        /* lifecycleScope.launch(Dispatchers.IO) {
                val weather = repo.getCurrentWeather().main
            Log.i("TAG2", "onCreate: ${weather}")
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val forecast = repo.getForecast().list[5]
            Log.i("TAG", "onCreate: ${forecast}")
        }*/

    }
}
@Composable
fun WeatherScreen() {
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
            ToDayWeatherCard("vbv", "gfgg", R.drawable.baseline_cloudy_snowing_24)
            Spacer(modifier = Modifier.height(16.dp))
            ToDayDetailsCard("gg", "gg", R.drawable.baseline_cloudy_snowing_24)

            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(getMockWeatherData()) { weather ->
                    RestOfDay(
                        label = weather.day,
                        value = weather.temperature,
                        icon = weather.icon
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { showBottomSheet.value = true }
            ) {
                Text("Display partial bottom sheet")
            }
        }

        if (showBottomSheet.value) {
            PartialBottomSheet(showBottomSheet)
        }
    }
}

@Composable
fun ToDayWeatherCard(label: String, value: String, icon: Int) {
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
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
@Composable
fun ToDayDetailsCard(label: String, value: String, icon: Int) {
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
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
@Composable
fun RestOfDay(label: String, value: String, icon: Int) {
    Card(
        modifier = Modifier.width(100.dp).height(200.dp)
            .padding(vertical = 16.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.3f)
        ),

        ) {
        Column (
            modifier = Modifier.padding(16.dp),
            //verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun WeatherCard(label: String, value: String, icon: Int) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.3f)
        ),

    ) {
        Column (
            modifier = Modifier.padding(16.dp),
            //verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
@Composable
fun NextDaysWeather() {
    Column {
        Text(
            text = "Next 7 Days",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        LazyColumn {
            items(getMockWeatherData()) { weather ->
                WeatherCard(
                    label = weather.day,
                    value = weather.temperature,
                    icon = weather.icon
                )
            }
        }
    }
}
data class WeatherInfo(val day: String, val temperature: String, val icon: Int)

fun getMockWeatherData(): List<WeatherInfo> {
    return listOf(
        WeatherInfo("Wednesday", "22°C", R.drawable.baseline_cloudy_snowing_24),
        WeatherInfo("Thursday", "21°C", R.drawable.baseline_cloudy_snowing_24),
        WeatherInfo("Friday", "24°C", R.drawable.baseline_cloudy_snowing_24),
        WeatherInfo("Saturday", "18°C", R.drawable.baseline_cloudy_snowing_24),
        WeatherInfo("Sunday", "12°C", R.drawable.baseline_cloudy_snowing_24),
        WeatherInfo("Monday", "16°C", R.drawable.baseline_cloudy_snowing_24),
        WeatherInfo("Tuesday", "18°C", R.drawable.baseline_cloudy_snowing_24),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartialBottomSheet(showBottomSheet: MutableState<Boolean>) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    if (showBottomSheet.value) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet.value = false }
        ) {
            NextDaysWeather()
        }
    }
}
