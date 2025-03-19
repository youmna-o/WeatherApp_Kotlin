package com.example.weatherapp.data.repo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHeloer {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        private val retrofitInstance = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

       val apiService = retrofitInstance.create(WeatherServices::class.java)

}