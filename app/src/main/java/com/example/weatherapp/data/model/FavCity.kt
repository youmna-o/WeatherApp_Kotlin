package com.example.weatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavCity (
    @PrimaryKey val name:String ="",
    val lat:Double=0.0,
    val lon:Double=0.0
    )