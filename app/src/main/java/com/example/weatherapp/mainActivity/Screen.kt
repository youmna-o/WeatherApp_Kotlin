package com.example.weatherapp.mainActivity

sealed class Screen(val rout: String) {
    object Weather: Screen("weather_screen")
    object Settings: Screen("settings_screen")
    object Favourite: Screen("favourite_screen")
    object Notification: Screen("notification_screen")
}