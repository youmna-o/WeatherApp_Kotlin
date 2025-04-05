
package com.example.weatherapp.weatherScreen


import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.repo.Repo
import com.google.android.gms.maps.model.LatLng
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class WeatherDetailsViewModelTest{
    private lateinit var  viewModel:WeatherDetailsViewModel
    lateinit var repo: Repo

    @Before
    fun setUp() {
        repo= mockk(relaxed = true)
        val app = ApplicationProvider.getApplicationContext<Application>()
        viewModel = WeatherDetailsViewModel(repo, app)
    }

    @Test
    fun setUserLocation_latLng_notNullLatLng() {
        val latLng = LatLng(30.0444, 31.2357)
        val result = viewModel.setUserLocation(latLng)
        val lat=viewModel.userLocation.value?.latitude
        val lon=viewModel.userLocation.value?.longitude
        assertNotNull(result)
        assertEquals(lat,latLng.latitude)
        assertEquals(lon,latLng.longitude)
    }
    @Test
    fun getCurrentWeatherByCoord_latlonlangunit_weatherData(){
        val lat = 30.0444
        val lon=31.2357
        val lang = "en"
        val unit="metric"
        val result=viewModel.getCurrentWeatherByCoord(lat = lat,lon=lon,lang=lang,unit=unit)
        val temp=viewModel.temp
        val wind=viewModel.wind
        assertNotNull(result)
        assertNotNull(temp)
        assertNotNull(wind)

    }



}
