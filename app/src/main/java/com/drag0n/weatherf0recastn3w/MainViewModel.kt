package com.drag0n.weatherf0recastn3w

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drag0n.weatherf0recastn3w.domane.API.GetApiNameCityNow
import com.drag0n.weatherf0recastn3w.domane.API.GetApiNameCityWeek
import com.drag0n.weatherf0recastn3w.domane.API.GetGeoNew
import com.drag0n.weatherf0recastn3w.domane.API.RepositoryImp
import kotlinx.coroutines.launch


class MainViewModel(private val app: Application): AndroidViewModel(app) {


    private val repository = RepositoryImp
    val liveDataDayNow = repository.liveDataCurrent
    val liveDataWeek = repository.liveDataCurrentWeek
    val load = repository.load



    private val getApiNameCityNow = GetApiNameCityNow(repository)
    private val getApiNameCityWeek = GetApiNameCityWeek(repository)
    private val getGeoNew = GetGeoNew(repository)

    fun getApiNameCitiNow(city: String){
        viewModelScope.launch {getApiNameCityNow.getApiNameCity(city, app.applicationContext)}
    }
    fun getApiNameCitiWeek(city: String){
        viewModelScope.launch {getApiNameCityWeek.getApiNameCityWeek(city, app.applicationContext)}
    }

    fun getGeoNew(lat: String, lon: String){
        viewModelScope.launch {  getGeoNew.getGeoNew(lat, lon, app.applicationContext) }

    }
}