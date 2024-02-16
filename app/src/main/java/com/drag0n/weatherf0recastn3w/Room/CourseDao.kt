package com.drag0n.weatherf0recastn3w.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Query("SELECT * FROM ItemCity")
    fun getAll(): Flow<List<ItemCity>>
    @Insert
    fun insertAll(itemCity: ItemCity)
    @Delete
    fun delete(itemCity: ItemCity)

}