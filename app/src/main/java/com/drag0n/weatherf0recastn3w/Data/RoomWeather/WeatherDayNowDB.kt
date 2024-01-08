package com.drag0n.weatherf0recastn3w.Data.RoomWeather

import androidx.room.Database
import androidx.room.RoomDatabase
import com.drag0n.weatherf0recastn3w.Data.WeatherDayNow.WeatherDayNow

@Database(entities = [WeatherDayNowDbImp::class], version = 1)
abstract class WeatherDayNowDB: RoomDatabase() {
    abstract fun CourseDao(): CourseDao

}