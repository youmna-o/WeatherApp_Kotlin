package com.example.weatherapp.mainActivity

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.R
import com.example.weatherapp.data.remote.RetrofitHeloer
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.repo.Repo
import com.example.weatherapp.favorite.FavouritScreen
import com.example.weatherapp.map.MapViewModel
import com.example.weatherapp.notifications.NotificationScreen
import com.example.weatherapp.settings.Settings
import com.example.weatherapp.ui.theme.myOrange
import com.example.weatherapp.weatherScreen.WeatherDetailsScreen
import com.example.weatherapp.weatherScreen.WeatherDetailsViewModel
import com.example.weatherapp.weatherScreen.myFactory


@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowNavBar(activity: ComponentActivity, application: Application,currentLat:Double ,currentLon:Double,mapViewModel: MapViewModel) {
    val navController = rememberNavController()
    val factory = myFactory(Repo(WeatherRemoteDataSource(RetrofitHeloer.apiService)),application)
    val viewModel: WeatherDetailsViewModel = ViewModelProvider(activity, factory)
        .get(WeatherDetailsViewModel::class.java)

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Blue)
        ) {
            Image(
                painter = painterResource(R.drawable.b1),
                contentDescription = "nn", Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            NavHost(
                navController = navController,
                startDestination = Screen.Notification.rout,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Settings.rout) { Settings(viewModel) }
                composable(Screen.Weather.rout) { WeatherDetailsScreen(viewModel,currentLat,currentLon) }
                composable(Screen.Favourite.rout) { FavouritScreen(mapViewModel) }
                composable(Screen.Notification.rout) { NotificationScreen() }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navigationItems = listOf(
        NavigationItem(stringResource(R.string.weather), R.drawable.partly_cloudy_day_24dp_5f6368_fill0_wght400_grad0_opsz24, Screen.Weather.rout),
        NavigationItem(stringResource(R.string.settings), Icons.Default.Settings, Screen.Settings.rout),
        NavigationItem(stringResource(R.string.favourite), Icons.Default.Favorite, Screen.Favourite.rout),
        NavigationItem(stringResource(R.string.notification), Icons.Default.Notifications, Screen.Notification.rout)
    )

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = myOrange
        //  Color.Blue.copy(alpha = 0.3f)
    ) {
        navigationItems.forEach { item ->
            val isSelected = currentDestination == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    when (item.icon) {
                        is Int -> Image(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title
                        )
                        is ImageVector -> Icon(item.icon, contentDescription = item.title)
                    }
                },
                label = {
                    Text(
                        item.title,
                        color = if (isSelected) Color.Black else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}
