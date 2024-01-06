package com.drag0n.weatherf0recastn3w.domane

import android.content.Context

interface Repository {
    fun getApiNameCityWeek(city: String, con: Context)
    fun getApiNameCity(city: String, con: Context)
    fun getApiDayNowLocation(lat: String, lon: String, con: Context)
    fun getApiWeekLocation(lat: String, lon: String, con: Context)

}