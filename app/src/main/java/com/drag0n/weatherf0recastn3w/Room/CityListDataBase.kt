package com.drag0n.weatherf0recastn3w.Room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ItemCity::class],
    version = 4,
)
abstract class CityListDataBase: RoomDatabase() {
    abstract fun CourseDao(): CourseDao
}