/*
package com.example.weatherapp.weatherScreen

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.repo.Repo
import com.google.android.gms.maps.model.LatLng
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDetailsViewModelTest{
    private lateinit var  viewModel:WeatherDetailsViewModel
    lateinit var repo:Repo


    @Test
    fun setUserLocation_latLng_notNullLatLng(){
        val app = ApplicationProvider.getApplicationContext<Application>()
        viewModel = WeatherDetailsViewModel(repo, app)
        val latLng= LatLng(30.0444, 31.2357)
        var result =viewModel.setUserLocation(latLng)
        assertThat(result ,`is`(not(nullValue())))
    }


}*/
