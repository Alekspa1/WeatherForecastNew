package com.drag0n.weatherf0recastn3w

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object Const {
    val baner = "R-M-3040873-1"
    val mezstr = "R-M-3040873-2"
    const val APIKEY = "05310d8fcd5b6c6cf16517801b3aa300"
    var lat = ""
    var lon = ""
    fun isPermissionGranted(con: Context,p: String): Boolean {
        return ContextCompat.checkSelfPermission(con, p) == PackageManager.PERMISSION_GRANTED
    }


}