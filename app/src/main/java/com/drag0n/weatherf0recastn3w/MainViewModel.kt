package com.drag0n.weatherf0recastn3w

import android.content.Context
import androidx.lifecycle.ViewModel
import com.drag0n.weatherf0recastn3w.domane.Api.GetApiNameCityNow
import com.drag0n.weatherf0recastn3w.domane.Api.GetApiNameCityWeek
import com.drag0n.weatherf0recastn3w.domane.Api.GetGeoNew
import com.drag0n.weatherf0recastn3w.domane.Api.RepositoryImp
import com.drag0n.weatherf0recastn3w.domane.TurnVibro.TurnFlashLightImpl
import com.drag0n.weatherf0recastn3w.domane.TurnVibro.TurnVibro


class MainViewModel: ViewModel() {


    private val repository = RepositoryImp
    private val repositoryVibro = TurnFlashLightImpl
    val liveDataDayNow = repository.liveDataCurrent
    val liveDataWeek = repository.liveDataCurrentWeek



    private val getApiNameCityNow = GetApiNameCityNow(repository)
    private val getApiNameCityWeek = GetApiNameCityWeek(repository)
    private val getGeoNew = GetGeoNew(repository)
    private val turnVibro = TurnVibro(repositoryVibro)

    fun getApiNameCitiNow(city: String, con: Context){
        getApiNameCityNow.getApiNameCity(city, con)
    }
    fun getApiNameCitiWeek(city: String, con: Context){
        getApiNameCityWeek.getApiNameCityWeek(city, con)
    }
    fun getGeoNew(lat: String, lon: String, con: Context){
        getGeoNew.getGeoNew(lat, lon, con)
    }
    fun turnVibro(con: Context, time: Long){
        turnVibro.turnVibro(con, time)
    }
}