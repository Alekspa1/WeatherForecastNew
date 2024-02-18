package com.drag0n.weatherf0recastn3w.domane.TurnVibro

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager

object TurnFlashLightImpl: TurnFlashLightAndVibro {
    private lateinit var vbManager: VibratorManager
    override fun turnVibro(con: Context, time: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            vbManager = con.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        }
        val vibro = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            vbManager.defaultVibrator
        } else {
            TODO("VERSION.SDK_INT < S")
        }
        vibro.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}