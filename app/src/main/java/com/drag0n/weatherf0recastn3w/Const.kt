package com.drag0n.weatherf0recastn3w

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import java.util.Locale

object Const {
    const val baner = "R-M-3040873-1"
    const val mezstr = "R-M-3040873-2"
    const val APIKEY = "05310d8fcd5b6c6cf16517801b3aa300"
    const val SEARCH_CITY = "SEARCH_CITY"
    const val DELETE_CITY = "DELETE_CITY"
    var language = Locale.getDefault().language.toString()
    fun isPermissionGranted(con: Context,p: String): Boolean {
        return ContextCompat.checkSelfPermission(con, p) == PackageManager.PERMISSION_GRANTED
    }


}