package com.drag0n.weatherf0recastn3w

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drag0n.weatherf0recastn3w.domane.API.GetApiNameCityNow
import com.drag0n.weatherf0recastn3w.domane.API.GetApiNameCityWeek
import com.drag0n.weatherf0recastn3w.domane.API.GetGeoNew
import com.drag0n.weatherf0recastn3w.domane.API.RepositoryImp
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {


    private val repository = RepositoryImp
    val liveDataDayNow = repository.liveDataCurrent
    val liveDataWeek = repository.liveDataCurrentWeek
    val load = repository.load
    val premium = MutableLiveData<Boolean>()




    private val getApiNameCityNow = GetApiNameCityNow(repository)
    private val getApiNameCityWeek = GetApiNameCityWeek(repository)
    private val getGeoNew = GetGeoNew(repository)

    fun getApiNameCitiNow(city: String, context: Context){
        viewModelScope.launch {getApiNameCityNow.getApiNameCity(city, context)}
    }
    fun getApiNameCitiWeek(city: String, context: Context){
        viewModelScope.launch {getApiNameCityWeek.getApiNameCityWeek(city, context)}
    }

    fun getGeoNew(lat: String, lon: String, context: Context){
        viewModelScope.launch {  getGeoNew.getGeoNew(lat, lon, context) }

    }
}