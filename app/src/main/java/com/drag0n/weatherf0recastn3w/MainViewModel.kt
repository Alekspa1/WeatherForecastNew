package com.drag0n.weatherf0recastn3w

import android.content.Context
import androidx.lifecycle.ViewModel
import com.drag0n.weatherf0recastn3w.domane.API.GetApiNameCityNow
import com.drag0n.weatherf0recastn3w.domane.API.GetApiNameCityWeek
import com.drag0n.weatherf0recastn3w.domane.API.GetGeoNew
import com.drag0n.weatherf0recastn3w.domane.API.RepositoryImp


class MainViewModel: ViewModel() {


    private val repository = RepositoryImp
    val liveDataDayNow = repository.liveDataCurrent
    val liveDataWeek = repository.liveDataCurrentWeek



    private val getApiNameCityNow = GetApiNameCityNow(repository)
    private val getApiNameCityWeek = GetApiNameCityWeek(repository)
    private val getGeoNew = GetGeoNew(repository)

    fun getApiNameCitiNow(city: String, con: Context){
        getApiNameCityNow.getApiNameCity(city, con)
    }
    fun getApiNameCitiWeek(city: String, con: Context){
        getApiNameCityWeek.getApiNameCityWeek(city, con)
    }
    fun getGeoNew(lat: String, lon: String, con: Context){
        getGeoNew.getGeoNew(lat, lon, con)
    }
}