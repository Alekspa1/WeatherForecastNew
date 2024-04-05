package com.drag0n.weatherf0recastn3w.domane.API

import android.content.Context

class GetApiNameCityWeek(private val repository: Repository) {
    suspend fun getApiNameCityWeek(city: String, con: Context){
        repository.getApiNameCityWeek(city, con)
    }
}