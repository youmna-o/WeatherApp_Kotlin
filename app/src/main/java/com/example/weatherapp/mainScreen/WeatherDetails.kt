package com.example.weatherapp.mainScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.remote.RetrofitHeloer
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.repo.Repo
import com.example.weatherapp.ui.theme.WeatherAppTheme

class WeatherDetails : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val factory = myFactory(
                Repo(
                    WeatherRemoteDataSource(RetrofitHeloer.apiService),
                )
            )
            val viewModel = ViewModelProvider(this, factory).get(WeatherDetailsViewModel::class.java)
            getWeather(viewModel)
        }
    }
}
@Composable
private fun getWeather(viewModel: WeatherDetailsViewModel){
    val weatherState = viewModel.weather.observeAsState(initial = null)
    LaunchedEffect(Unit) {
        viewModel.getCurrentWeather()
    }
    if (weatherState.value != null) {
        WeatherScreen(weatherState.value!!)


    }
}
@Composable
private fun getForecast(viewModel: WeatherDetailsViewModel){
    val foreCastState = viewModel.forecast.observeAsState()
    LaunchedEffect(Unit) {
        viewModel.getForecast()
    }
    if (foreCastState.value != null) {

       // NextDaysWeather(foreCastState.value!!)


    }
}
@Composable
fun WeatherScreen(weatherData: WeatherData) {
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
            ToDayWeatherCard("*************", "${weatherData.visibility}", R.drawable.baseline_cloudy_snowing_24)
            Spacer(modifier = Modifier.height(16.dp))
            ToDayDetailsCard("gg", "${weatherData.main?.temp}", R.drawable.baseline_cloudy_snowing_24)

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


