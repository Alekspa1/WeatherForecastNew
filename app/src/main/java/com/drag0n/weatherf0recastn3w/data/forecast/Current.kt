package com.drag0n.weatherf0recastn3w.data.forecast

data class Current(
    val condition: Condition,
    val feelslike_c: Double,
    val humidity: Int,
    val is_day: Int,
    val pressure_mb: Double,
    val temp_c: Double,
    val wind_mph: Double
)