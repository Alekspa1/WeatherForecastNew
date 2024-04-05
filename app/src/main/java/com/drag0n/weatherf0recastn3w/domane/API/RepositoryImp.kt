package com.drag0n.weatherf0recastn3w.domane.API

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.drag0n.weatherf0recastn3w.Const
import com.drag0n.weatherf0recastn3w.Const.language
import com.drag0n.weatherf0recastn3w.data.WeatherDayNow.WeatherDayNow
import com.drag0n.weatherf0recastn3w.data.WeatherWeek.WeatherWeek
import com.drag0n.weatherf0recastn3w.R
import com.drag0n.weatherf0recastn3w.presentation.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

object RepositoryImp: Repository {

    val liveDataCurrent = MutableLiveData<WeatherDayNow>()
    val liveDataCurrentWeek = MutableLiveData<WeatherWeek>()
    val load = MutableLiveData<Boolean>()

    override suspend fun getApiNameCityWeek(city: String, con: Context) {
        try { val response = ApiWeather.create().getWeatherWeekCity(city, Const.APIKEY, language)
            if (response.isSuccessful){
                val resuly = response.body()
                liveDataCurrentWeek.value = resuly!!
            } }


        catch (_: UnknownHostException){

        }

    }

    override suspend fun getApiNameCity(city: String, con: Context) {
        try { val response = ApiWeather.create().getWeatherDayNowCity(city, Const.APIKEY, language)
            if (response.isSuccessful){
                val resuly = response.body()
                liveDataCurrent.value = resuly!!
            } else{
                load.value = false
                Toast.makeText(con, con.getString(R.string.repository_error_data_onResponse), Toast.LENGTH_SHORT).show()}
        }
        catch (e: Exception){
            load.value = false
            Toast.makeText(con, con.getString(R.string.repository_error_data_onFailure), Toast.LENGTH_SHORT).show()
        }
    }


    override suspend fun getGeoNew(lat: String, lon: String, con: Context) {
        try { val response = ApiWeather.create().getGeoNowNew(lat, lon, Const.APIKEY)
            if (response.isSuccessful){
                val resuly = response.body()
                getApiDayNowLocation(resuly?.get(0)?.lat.toString(), resuly?.get(0)?.lon.toString(), con)
                getApiWeekLocation(resuly?.get(0)?.lat.toString(), resuly?.get(0)?.lon.toString())
            } else{
                load.value = false
                Toast.makeText(con, con.getString(R.string.repository_error_data_onResponse), Toast.LENGTH_SHORT).show()}
        }
        catch (e: Exception){
            Log.d("MyLog", e.message.toString())
           load.value = false
            Toast.makeText(con, con.getString(R.string.repository_error_data_onFailure), Toast.LENGTH_SHORT).show()
        }
    }
     private suspend fun getApiDayNowLocation(lat: String, lon: String, con: Context) {
         try { val response = ApiWeather.create().getWeatherDayNowLocation(lat, lon, Const.APIKEY, language)
             if (response.isSuccessful){
                 val resuly = response.body()
                 liveDataCurrent.value = resuly!!
             } else{
                 load.value = false
                 Toast.makeText(con, con.getString(R.string.repository_error_data_onResponse), Toast.LENGTH_SHORT).show()}
         }
         catch (e: Exception){
             load.value = false
             Toast.makeText(con, con.getString(R.string.repository_error_data_onFailure), Toast.LENGTH_SHORT).show()
         }

    }

     private suspend fun getApiWeekLocation(lat: String, lon: String) {
         try { val response = ApiWeather.create().getWeatherWeekLocation(lat, lon, Const.APIKEY, language)
             if (response.isSuccessful){
                 val resuly = response.body()
                 liveDataCurrentWeek.value = resuly!!
             }
         }
         catch (_: UnknownHostException){

         }

    }
}

