package com.drag0n.weatherf0recastn3w.domane.Api

import android.content.Context

class GetApiNameCityWeek(private val repository: Repository) {
    fun getApiNameCityWeek(city: String, con: Context){
        repository.getApiNameCityWeek(city, con)
    }
}