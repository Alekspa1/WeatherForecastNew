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

    override fun getApiNameCityWeek(city: String, con: Context) {
        val apiInterface = ApiWeather.create().getWeatherWeekCity(city, Const.APIKEY, language)
        apiInterface.enqueue(object : Callback<WeatherWeek> {

            override fun onResponse(call: Call<WeatherWeek>, response: Response<WeatherWeek>) {
                val data = response.body()
                if (data != null) liveDataCurrentWeek.value = data!!
                else Toast.makeText(
                    con,
                    con.getString(R.string.repository_error_data_onResponse),
                    Toast.LENGTH_SHORT
                ).show()

            }

            override fun onFailure(call: Call<WeatherWeek>, t: Throwable) {
                Toast.makeText(
                    con,
                    con.getString(R.string.repository_error_data_onFailure),
                    Toast.LENGTH_SHORT
                ).show()

            }
        })

    }

    override fun getApiNameCity(city: String, con: Context) {
        val apiInterface = ApiWeather.create().getWeatherDayNowCity(city, Const.APIKEY, language)
        apiInterface.enqueue(object : Callback<WeatherDayNow> {

            override fun onResponse(call: Call<WeatherDayNow>, response: Response<WeatherDayNow>) {
                val data = response.body()
                if (data != null) liveDataCurrent.value = data!!

                else Toast.makeText(
                    con,
                    con.getString(R.string.repository_error_data_onResponse),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<WeatherDayNow>, t: Throwable) {
                Toast.makeText(
                    con,
                    con.getString(R.string.repository_error_data_onFailure),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    override suspend fun getGeoNew(lat: String, lon: String, con: Context) {
        try { val response = ApiWeather.create().getGeoNowNew(lat, lon, Const.APIKEY)
            if (response.isSuccessful){
                val resuly = response.body()
                getApiDayNowLocation(resuly?.get(0)?.lat.toString(), resuly?.get(0)?.lon.toString(), con)
                getApiWeekLocation(resuly?.get(0)?.lat.toString(), resuly?.get(0)?.lon.toString(), con)
            } else{
                Toast.makeText(con, con.getString(R.string.repository_error_data_onResponse), Toast.LENGTH_SHORT).show()}
        }
        catch (e: UnknownHostException){
           load.value = false
            Toast.makeText(con, con.getString(R.string.repository_error_data_onFailure), Toast.LENGTH_SHORT).show()
        }
    }
     fun getApiDayNowLocation(lat: String, lon: String, con: Context) {
        val apiInterface = ApiWeather.create().getWeatherDayNowLocation(lat, lon, Const.APIKEY, language)
        apiInterface.enqueue(object : Callback<WeatherDayNow> {

            override fun onResponse(call: Call<WeatherDayNow>, response: Response<WeatherDayNow>) {
                val data = response.body()
                if (data != null) {
                    liveDataCurrent.value = data!!
                } else Toast.makeText(
                    con,
                    con.getString(R.string.repository_error_data_onResponse),
                    Toast.LENGTH_SHORT
                ).show()


            }

            override fun onFailure(call: Call<WeatherDayNow>, t: Throwable) {

            }
        })
    }

     fun getApiWeekLocation(lat: String, lon: String, con: Context) {
        val apiInterface = ApiWeather.create().getWeatherWeekLocation(lat, lon, Const.APIKEY, language)
        apiInterface.enqueue(object : Callback<WeatherWeek> {

            override fun onResponse(call: Call<WeatherWeek>, response: Response<WeatherWeek>) {
                val data = response.body()
                if (data != null) liveDataCurrentWeek.value = data!!
                else Toast.makeText(
                    con,
                    con.getString(R.string.repository_error_data_onResponse),
                    Toast.LENGTH_SHORT
                ).show()

            }

            override fun onFailure(call: Call<WeatherWeek>, t: Throwable) {
                Toast.makeText(
                    con,
                    con.getString(R.string.repository_error_data_onFailure),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}

