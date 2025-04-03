package com.example.weatherapp.data.source

import androidx.compose.runtime.MutableState
import com.example.weatherapp.data.local.CityLocalDataSource
import com.example.weatherapp.data.local.ICityLocalDataSource
import com.example.weatherapp.data.model.City
import com.example.weatherapp.data.model.FavCity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeLocalDataSource(private val citiesList: MutableList<FavCity> = mutableListOf()) : ICityLocalDataSource {
    private val citiesFlow = MutableStateFlow<List<FavCity>>(citiesList)

    override suspend fun getAll(): Flow<List<FavCity>> {
    return citiesFlow.asStateFlow()
    }

    override suspend fun insertCity(city: FavCity): Long {
        TODO("Not yet implemented")

    }

    override suspend fun delete(city: FavCity): Int {
        return if (citiesList.remove(city)) {
            citiesFlow.value = citiesList
            1
        } else {
          0
        }
    }
}
