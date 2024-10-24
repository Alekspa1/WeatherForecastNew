package com.drag0n.weatherf0recastn3w

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import java.util.Locale

object Const {
    const val baner = "R-M-3040873-1"
    const val mezstr = "R-M-3040873-2"
    const val APIKEY = "adf5d4c2d3cc454c9dd113058231009"
    const val SEARCH_CITY = "SEARCH_CITY"
    const val DELETE_CITY = "DELETE_CITY"

    const val PREMIUM_KEY = "premium_KEY"
    const val RUSTORE = "https://apps.rustore.ru/app/com.drag0n.weatherf0recastn3w"
    const val APP_GALERY = "https://weatherforecast.drru.agconnect.link/Xmx7"

    var LANGUAGE = Locale.getDefault().language.toString()
    fun isPermissionGranted(con: Context,p: String): Boolean {
        return ContextCompat.checkSelfPermission(con, p) == PackageManager.PERMISSION_GRANTED
    }


}