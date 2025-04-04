package com.example.weatherapp.notifications

data class ReminderItem(
    val time: Long,
    val id: Int,
    val lat:Double,
    val lon: Double
)