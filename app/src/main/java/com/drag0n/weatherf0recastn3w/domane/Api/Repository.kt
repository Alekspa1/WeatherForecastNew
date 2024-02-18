package com.drag0n.weatherf0recastn3w.domane.Api

import android.content.Context

interface Repository {
    fun getApiNameCityWeek(city: String, con: Context)
    fun getApiNameCity(city: String, con: Context)
    fun getGeoNew(lat: String, lon: String, con: Context)

}