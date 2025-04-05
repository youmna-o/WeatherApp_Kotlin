package com.example.weatherapp.data.local

import com.example.weatherapp.data.model.FavCity
import kotlinx.coroutines.flow.Flow


class CityLocalDataSource (private val dao:CityDao) : ICityLocalDataSource {
    override suspend fun getAll(): Flow<List<FavCity>> {
        return  dao.getAll()
    }
    override suspend fun insertCity(city: FavCity): Long {
        val result = dao.insertCity(city)
        return if (result != -1L)
            result
        else
            -1L
    }
    override suspend fun delete(city: FavCity):Int{
        return if(city!=null)
            dao.delete(city)
        else{
            -1
        }
    }
}