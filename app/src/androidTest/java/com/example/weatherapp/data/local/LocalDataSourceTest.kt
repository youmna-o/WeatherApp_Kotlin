package com.example.weatherapp.data.local
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherapp.data.model.FavCity
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class LocalDataSourceTest{
    private  lateinit var dao: CityDao
    private lateinit var dataBase: CityDataBase
    private lateinit var cityLocalDataSource: CityLocalDataSource
    @Before
    fun setUp(){
        dataBase= Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CityDataBase::class.java).allowMainThreadQueries()
            .build()
        dao=dataBase.getCityDao()
        cityLocalDataSource= CityLocalDataSource(dao)
    }
    @After
    fun close()=dataBase.close()
    @Test
    fun insertCity_retrieveCity() = runTest {
        val city = FavCity(name = "cairo")
        val insertedId = cityLocalDataSource.insertCity(city)
        assertNotNull(insertedId)
        assertThat(insertedId, `is`(not(-1L)))
        val retrievedCities = dao.getAll().first()
        assertTrue(retrievedCities.isNotEmpty())
        assertThat(retrievedCities.first().name, `is`("cairo"))
    }
    @Test
    fun deleteCity_city_1() = runTest {
        val city = FavCity(name = "cairo")
        cityLocalDataSource.insertCity(city)
        val result = cityLocalDataSource.delete(city)
        assertNotNull(result)
        assertThat(result, `is`(1))
        val citiesAfterDeletion = dao.getAll().first()
        assertTrue(citiesAfterDeletion.isEmpty())

    }






}