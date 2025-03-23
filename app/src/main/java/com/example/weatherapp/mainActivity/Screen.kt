package com.example.weatherapp.mainActivity

sealed class Screen(val rout: String) {
    object Weather: Screen("weather_screen")
    object Favourite: Screen("favourite_screen")
    object Map: Screen("map_screen")
    object Notification: Screen("notification_screen")
}