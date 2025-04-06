package com.example.weatherapp.data.local

import com.example.weatherapp.data.model.FavCity
import kotlinx.coroutines.flow.Flow

interface ICityLocalDataSource {
    suspend fun getAll(): Flow<List<FavCity>>
    suspend fun insertCity(city: FavCity): Long
    suspend fun delete(city: FavCity): Int
}