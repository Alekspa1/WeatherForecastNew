package com.drag0n.weatherf0recastn3w.data.WeatherDayNow

data class WeatherDayNow(
   val main: Main,
   val name: String,
   val weather: List<Weather>,
   val wind: Wind,
   val sys: Sys,
   val timezone: Int,
   val coord: Coord
)