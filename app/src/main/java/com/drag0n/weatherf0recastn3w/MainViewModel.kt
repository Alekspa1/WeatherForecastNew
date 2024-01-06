package com.drag0n.weatherf0recastn3w

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.drag0n.weatherf0recastn3w.Data.WeatherDayNow.WeatherDayNow
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.WeatherWeek
import com.drag0n.weatherf0recastn3w.domane.GetApiDayNowLocation
import com.drag0n.weatherf0recastn3w.domane.GetApiNameCityNow
import com.drag0n.weatherf0recastn3w.domane.GetApiNameCityWeek
import com.drag0n.weatherf0recastn3w.domane.GetApiWeekLocation
import com.drag0n.weatherf0recastn3w.domane.RepositoryImp

class MainViewModel: ViewModel() {


    private val repository = RepositoryImp
    val liveDataDayNow = repository.liveDataCurrent
    val liveDataWeek = repository.liveDataCurrentWeek


    private val getApiNameCityNow = GetApiNameCityNow(repository)
    private val getApiNameCityWeek = GetApiNameCityWeek(repository)
    private val getApiDayNowLocation = GetApiDayNowLocation(repository)
    private val getApiWeekLocation = GetApiWeekLocation(repository)

    fun getApiNameCitiNow(city: String, con: Context){
        getApiNameCityNow.getApiNameCity(city, con)
    }
    fun getApiNameCitiWeek(city: String, con: Context){
        getApiNameCityWeek.getApiNameCityWeek(city, con)
    }

    fun getApiDayNowLocation(lat: String, lon: String, con: Context){
        getApiDayNowLocation.getApiDayNowLocation(lat, lon, con)
    }

    fun getApiWeekLocation(lat: String, lon: String, con: Context){
        getApiWeekLocation.getApiWeekLocation(lat, lon, con)
    }
}