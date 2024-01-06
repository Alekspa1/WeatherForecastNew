package com.drag0n.weatherf0recastn3w.domane

import android.content.Context

class GetApiDayNowLocation(private val repository: Repository) {
    fun getApiDayNowLocation(lat: String, lon: String, con: Context){
        repository.getApiDayNowLocation(lat, lon, con)

    }
}