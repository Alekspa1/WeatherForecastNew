package com.drag0n.weatherf0recastn3w.domane

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.drag0n.weatherf0recastn3w.ApiWeather
import com.drag0n.weatherf0recastn3w.Const

import com.drag0n.weatherf0recastn3w.Data.WeatherDayNow.WeatherDayNow
import com.drag0n.weatherf0recastn3w.Data.WeatherGetGeo.GetGeoNew
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.WeatherWeek
import com.drag0n.weatherf0recastn3w.Room.CityListDataBase
import com.drag0n.weatherf0recastn3w.Room.ItemCity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RepositoryImp: Repository {

    val liveDataCurrent = MutableLiveData<WeatherDayNow>()
    val liveDataCurrentWeek = MutableLiveData<WeatherWeek>()
    override fun getApiNameCityWeek(city: String, con: Context) {
        val apiInterface = ApiWeather.create().getWeatherWeekCity(city, Const.APIKEY)
        apiInterface.enqueue(object : Callback<WeatherWeek> {

            override fun onResponse(call: Call<WeatherWeek>, response: Response<WeatherWeek>) {
                val data = response.body()
                if (data != null) liveDataCurrentWeek.value = data!!
                else Toast.makeText(
                    con,
                    "Ошибка получения данных",
                    Toast.LENGTH_SHORT
                ).show()

            }

            override fun onFailure(call: Call<WeatherWeek>, t: Throwable) {
                Toast.makeText(
                    con,
                    "Данные недоступны, попробуйте позже",
                    Toast.LENGTH_SHORT
                ).show()

            }
        })

    }

    override fun getApiNameCity(city: String, con: Context) {
        val apiInterface = ApiWeather.create().getWeatherDayNowCity(city, Const.APIKEY)
        apiInterface.enqueue(object : Callback<WeatherDayNow> {

            override fun onResponse(call: Call<WeatherDayNow>, response: Response<WeatherDayNow>) {
                val data = response.body()
                if (data != null) {
                    liveDataCurrent.value = data!!
                }

                else Toast.makeText(
                    con,
                    "Ошибка получения данных",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<WeatherDayNow>, t: Throwable) {
                Toast.makeText(
                    con,
                    "Данные недоступны, попробуйте позже",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun getGeoNew(lat: String, lon: String, con: Context) {
        val apiInterface = ApiWeather.newGeo().getGeoNowNew(lat, lon, Const.APIKEY)
        apiInterface.enqueue(object : Callback<GetGeoNew> {

            override fun onResponse(call: Call<GetGeoNew>, response: Response<GetGeoNew>) {
                val data = response.body()
                if (data != null) {
                    getApiNameCity(data[0].name, con)
                    getApiNameCityWeek(data[0].name, con)
                } else Toast.makeText(
                    con,
                    "Ошибка получения данных",
                    Toast.LENGTH_SHORT
                ).show()


            }

            override fun onFailure(call: Call<GetGeoNew>, t: Throwable) {

            }
        })
    }
}

