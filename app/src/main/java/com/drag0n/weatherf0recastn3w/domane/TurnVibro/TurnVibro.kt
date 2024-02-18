package com.drag0n.weatherf0recastn3w.domane.TurnVibro

import android.content.Context

class TurnVibro(private val turnFlashLightAndVibro: TurnFlashLightAndVibro) {

    fun turnVibro(con: Context, time: Long){
        turnFlashLightAndVibro.turnVibro(con, time)
    }
}