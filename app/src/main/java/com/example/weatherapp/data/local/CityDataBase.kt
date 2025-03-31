package com.example.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.data.model.FavCity


@Database(entities = [FavCity::class] , version = 1)
abstract class CityDataBase : RoomDatabase() {
    abstract fun getCityDao(): CityDao

    companion object{
        @Volatile
        private var instanceOfDB : CityDataBase? = null
        fun getInstance(context: Context): CityDataBase {
            return instanceOfDB ?: synchronized(this){
                val temp: CityDataBase = Room.databaseBuilder(context,
                    CityDataBase::class.java,"citydb").build()
                instanceOfDB = temp
                temp
            }

        }
    }
}