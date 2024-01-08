package com.drag0n.weatherf0recastn3w.Data.RoomWeather

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.drag0n.weatherf0recastn3w.Data.WeatherDayNow.WeatherDayNow
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Query("SELECT * FROM WeatherDayNowDbImp")
    fun getAll(): WeatherDayNowDbImp

    @Insert
    fun insertAll(weatherDayNow: WeatherDayNowDbImp)

    @Delete
    fun delete(weatherDayNow: WeatherDayNowDbImp)
    @Update
    fun update(weatherDayNow: WeatherDayNowDbImp)



}