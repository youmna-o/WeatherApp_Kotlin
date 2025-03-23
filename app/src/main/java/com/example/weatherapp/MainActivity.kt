package com.example.weatherapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.repo.Repo
import com.example.weatherapp.favorite.FavouritScreen
import com.example.weatherapp.mainActivity.NavigationItem
import com.example.weatherapp.mainActivity.Screen
import com.example.weatherapp.map.MapScreen
import com.example.weatherapp.notifications.NotificationScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.weatherScreen.WeatherDetailsScreen
import com.example.weatherapp.weatherScreen.WeatherScreen


class MainActivity : ComponentActivity() {
    private lateinit var currentWeather: MutableState<WeatherData>
    private lateinit var foreCast: MutableState<ForecastData>
    private lateinit var repo: Repo
    @RequiresApi(Build.VERSION_CODES.O)
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
        /* Column {
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
         }*/
         }
         ShowNavBar(this)

     }

     }


    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowNavBar(activity: ComponentActivity) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Weather.rout,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Map.rout) { MapScreen() }
            composable(Screen.Weather.rout) { WeatherDetailsScreen(activity) }
            composable(Screen.Favourite.rout) { FavouritScreen() }
            composable(Screen.Notification.rout) { NotificationScreen() }
        }
    }
}
@Composable
fun BottomNavigationBar(navController: NavController) {
    val navigationItems = listOf(
        NavigationItem("Weather", Icons.Default.Home, Screen.Weather.rout),
        NavigationItem("Favourite", Icons.Default.Person, Screen.Favourite.rout),
        NavigationItem("Map", Icons.Default.ShoppingCart, Screen.Map.rout),
        NavigationItem("Notification", Icons.Default.Settings, Screen.Notification.rout)
    )

    NavigationBar(
        containerColor = Color.White
    ) {
        val selectedNavigationIndex = rememberSaveable { mutableIntStateOf(0) }
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedNavigationIndex.intValue == index,
                onClick = {
                    selectedNavigationIndex.intValue = index
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = {
                    Text(
                        item.title,
                        color = if (index == selectedNavigationIndex.intValue)
                            Color.Black
                        else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.surface,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
