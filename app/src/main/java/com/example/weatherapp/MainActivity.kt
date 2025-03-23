package com.example.weatherapp

import android.content.Intent
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.material3.Typography
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherapp.data.remote.RetrofitHeloer
import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.repo.Repo
import com.example.weatherapp.mainScreen.WeatherDetails
import com.example.weatherapp.ui.theme.Typography
import com.example.weatherapp.ui.theme.WeatherAppTheme
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
     WeatherAppTheme {
         val currentContext = LocalContext.current
         Box(
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
         Column {
             Button({
                 val intent = Intent(currentContext, WeatherDetails()::class.java).apply {}
                 currentContext.startActivity(intent)
             }) {
                 Text("weatherScreen")
             }
             Button({
                 val intent = Intent(currentContext, WeatherDetails()::class.java).apply {}
                 currentContext.startActivity(intent)
             }) {
                 Text("weatherScreen")
             }
         }
         }


     }




     }

        /* lifecycleScope.launch(Dispatchers.IO) {
                val weather = repo.getCurrentWeather(true).main
            Log.i("TAG2", "onCreate: ${weather}")
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val forecast = repo.getForecast().list[5]
            Log.i("TAG", "onCreate: ${forecast}")
        }*/

    }
}
