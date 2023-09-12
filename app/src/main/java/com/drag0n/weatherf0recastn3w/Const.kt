package com.drag0n.weatherf0recastn3w

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object Const {
    var city = "Астрахань"
    const val APIKEY = "05310d8fcd5b6c6cf16517801b3aa300"
    var lang = "ru"
    var lat = ""
    var lon = ""
    fun isPermissionGranted(con: Context,p: String): Boolean {
        return ContextCompat.checkSelfPermission(con, p) == PackageManager.PERMISSION_GRANTED
    }


}