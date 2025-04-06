package com.example.weatherapp.data
import com.example.weatherapp.data.model.FavCity
import com.example.weatherapp.data.repo.Repo
import com.example.weatherapp.data.source.FakeLocalDataSource
import com.example.weatherapp.data.source.FakeRemoteDataSource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class RepoTest {
    private var localData = mutableListOf(
        FavCity("cairo"),
        FavCity("London"),
        FavCity("paris"),
    )

    private lateinit var remote :FakeRemoteDataSource
    private lateinit var local :FakeLocalDataSource
    private lateinit var  repo:Repo
    @Before
    fun setUp(){
        remote=FakeRemoteDataSource()
        local=FakeLocalDataSource(localData)
        repo= Repo(remote,local)
    }

    @Test
    fun getFavCity_noInput_listOfFavCities()= runTest {
     val result = repo.getCurrentWeather("Cairo","en","metric").first()
        assertEquals("Cairo", result.name)
        result.main?.temp?.let { assertEquals( 299.57, it, 0.1) }
        assertEquals("Clear", result.weather[0].main)
    }
    @Test
    fun deleteCity_city_1()= runTest {
        val result = repo.delete(FavCity("London"))
      assertEquals(1,result)
    }

}