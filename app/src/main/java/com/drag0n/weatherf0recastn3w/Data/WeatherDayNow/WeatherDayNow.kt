package com.drag0n.weatherf0recastn3w.Data.WeatherDayNow

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class WeatherDayNow(
    @PrimaryKey var id: Int?,
    @ColumnInfo(name = "main")val main: Main,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "weather")val weather: List<Weather>,
)