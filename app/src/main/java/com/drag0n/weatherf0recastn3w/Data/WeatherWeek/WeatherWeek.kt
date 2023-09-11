package com.drag0n.weatherf0recastn3w.Data.WeatherWeek

data class WeatherWeek(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Spisok>,
    val message: Int
)