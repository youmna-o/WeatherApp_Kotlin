package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weatherapp.data.model.FavCity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM favcity")
    fun getAll(): Flow<List<FavCity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCity(city: FavCity): Long


    @Delete
    suspend fun delete(city: FavCity): Int

}