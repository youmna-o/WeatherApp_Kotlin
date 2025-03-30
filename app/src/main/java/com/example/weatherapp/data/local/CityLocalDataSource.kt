package com.example.weatherapp.data.local

import com.example.weatherapp.data.model.FavCity
import kotlinx.coroutines.flow.Flow


class CityLocalDataSource (private val dao:CityDao){
    suspend fun getAll(): Flow<List<FavCity>> {
        return  dao.getAll()
    }
    suspend fun insertCity(city: FavCity):Long{
        return  dao.insertCity(city)
    }
    suspend fun delete(city: FavCity):Int{
        return if(city!=null)
            dao.delete(city)
        else{
            -1
        }
    }
}