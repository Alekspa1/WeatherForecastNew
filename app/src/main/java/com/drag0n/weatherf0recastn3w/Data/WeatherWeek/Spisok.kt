package com.drag0n.weatherf0recastn3w.Data.WeatherWeek

data class Spisok (
    val dt_txt: String,
    val main: Main,
    val pop: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)