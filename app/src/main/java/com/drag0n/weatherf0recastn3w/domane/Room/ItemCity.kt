package com.drag0n.weatherf0recastn3w.domane.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemCity(
    @PrimaryKey var id: Int?,
    @ColumnInfo val name: String
)
