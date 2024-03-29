package com.drag0n.weatherf0recastn3w.domane.API

import android.content.Context

class GetApiNameCityNow(private val repository: Repository) {
    suspend fun getApiNameCity(city: String, con: Context){
        repository.getApiNameCity(city, con)
    }
}