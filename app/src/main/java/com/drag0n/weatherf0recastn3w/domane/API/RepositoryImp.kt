package com.drag0n.weatherf0recastn3w.domane.API

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.drag0n.weatherf0recastn3w.Const
import com.drag0n.weatherf0recastn3w.Const.language
import com.drag0n.weatherf0recastn3w.data.WeatherDayNow.WeatherDayNow
import com.drag0n.weatherf0recastn3w.data.WeatherGetGeo.GetGeoNew
import com.drag0n.weatherf0recastn3w.data.WeatherWeek.WeatherWeek
import com.drag0n.weatherf0recastn3w.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RepositoryImp: Repository {

    val liveDataCurrent = MutableLiveData<WeatherDayNow>()
    val liveDataCurrentWeek = MutableLiveData<WeatherWeek>()

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

    override fun getGeoNew(lat: String, lon: String, con: Context) {
        val apiInterface = ApiWeather.create().getGeoNowNew(lat, lon, Const.APIKEY)
        apiInterface.enqueue(object : Callback<GetGeoNew> {

            override fun onResponse(call: Call<GetGeoNew>, response: Response<GetGeoNew>) {
                val data = response.body()
                if (data != null) {
                    getApiDayNowLocation(data[0].lat.toString(), data[0].lon.toString(), con)
                    getApiWeekLocation(data[0].lat.toString(), data[0].lon.toString(), con)
                } else Toast.makeText(
                    con,
                    con.getString(R.string.repository_error_data_onResponse),
                    Toast.LENGTH_SHORT
                ).show()


            }

            override fun onFailure(call: Call<GetGeoNew>, t: Throwable) {

            }
        })
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

