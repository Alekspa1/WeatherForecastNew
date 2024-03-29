package com.drag0n.weatherf0recastn3w.domane.API

import android.content.Context

interface Repository {
    suspend fun getApiNameCityWeek(city: String, con: Context)
    suspend fun getApiNameCity(city: String, con: Context)
    suspend fun getGeoNew(lat: String, lon: String, con: Context)

}