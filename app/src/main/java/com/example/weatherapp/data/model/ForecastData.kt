package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName


data class ForecastData (

    @SerializedName("cod"     ) var cod     : String?         = null,
    @SerializedName("message" ) var message : Int?            = null,
    @SerializedName("cnt"     ) var cnt     : Int?            = null,
    @SerializedName("list"    ) var list    : ArrayList<forecastList> = arrayListOf(),
    @SerializedName("city"    ) var city    : City?           = City()

)