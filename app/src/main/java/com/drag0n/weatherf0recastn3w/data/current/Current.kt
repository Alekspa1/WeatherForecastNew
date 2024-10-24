package com.drag0n.weatherf0recastn3w.data.current

data class Current(
    val cloud: Int,
    val condition: Condition,
    val dewpoint_c: Double,
    val feelslike_c: Double,
    val gust_kph: Double,
    val gust_mph: Double,
    val heatindex_c: Double,
    val humidity: Int,
    val is_day: Int,
    val precip_in: Double,
    val precip_mm: Double,
    val pressure_in: Double,
    val pressure_mb: Double,
    val temp_c: Double,
    val temp_f: Double,
    val vis_km: Double,
    val wind_degree: Int,
    val wind_dir: String,
    val wind_mph: Double,
    val windchill_c: Double,
    val windchill_f: Double
)