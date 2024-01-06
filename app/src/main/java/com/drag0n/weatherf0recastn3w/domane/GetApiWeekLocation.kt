package com.drag0n.weatherf0recastn3w.domane

import android.content.Context

class GetApiWeekLocation(private val repository: Repository) {
    fun getApiWeekLocation(lat: String, lon: String, con: Context){
        repository.getApiWeekLocation(lat, lon, con)
    }
}