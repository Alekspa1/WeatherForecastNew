package com.drag0n.weatherf0recastn3w.Data.RoomWeather

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherDayNowDbImp(
    @PrimaryKey var id: Int?,
    @ColumnInfo(name = "date")val date: String,
    @ColumnInfo(name = "name")val name: String?,
    @ColumnInfo(name = "temp")val temp: String?,
    //@ColumnInfo(name = "weather")val weather: List<Weather>,
)