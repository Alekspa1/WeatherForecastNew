package com.drag0n.weatherf0recastn3w.domane.API

import android.content.Context

class GetGeoNew(private val repository: Repository) {
    suspend fun getGeoNew(lat: String, lon: String, con: Context){
        repository.getGeoNew(lat, lon, con)

    }
}