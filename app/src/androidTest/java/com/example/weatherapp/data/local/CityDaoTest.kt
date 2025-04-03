package com.example.weatherapp.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapp.data.model.FavCity
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CityDaoTest {
    private  lateinit var dao:CityDao
    private lateinit var dataBase:CityDataBase
    @Before
    fun setUp(){
        dataBase= Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CityDataBase::class.java).allowMainThreadQueries()
            .build()
        dao=dataBase.getCityDao()
    }
    @After
    fun close()=dataBase.close()

    @Test
    fun insertCity_insertedCity_longNotEqualminusOne()= runTest{
        val city =FavCity(name = "tanta")
        val sameCity =FavCity(name = "tanta")
        var result = dao.insertCity(city)
        var resultAfterRepeat = dao.insertCity(sameCity)

        assertNotNull(result)
        assertThat(result , `is`(not(-1L)))
        assertNotNull(resultAfterRepeat)
        assertThat(resultAfterRepeat , `is`(-1L))
    }
    @Test
    fun deleteCity_deletedCity_1()= runTest{
        val city =FavCity(name = "cairo")
        dao.insertCity(city)
        var result = dao.delete(city)

        assertNotNull(result)
        assertThat(result , `is`(not(-1L)))
        assertThat(result , `is`(1))

    }
}