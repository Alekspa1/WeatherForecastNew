package com.drag0n.weatherf0recastn3w

import com.drag0n.weatherf0recastn3w.Data.WeatherDayNow.WeatherDayNow
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.WeatherWeek
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiWeather {
    @GET("weather?&units=metric&lang=ru")
    fun getWeatherDayNow(
        @Query("q") city: String,
        @Query("appid") api: String
    ): Call<WeatherDayNow>

    @GET("forecast?&units=metric&lang=ru")
    fun getWeatherWeek(
        @Query("q") city: String,
        @Query("appid") api: String
    ): Call<WeatherWeek>



    companion object {

        var BASE_URL = "https://api.openweathermap.org/data/2.5/"
        val interceptor = HttpLoggingInterceptor()

        fun create() : ApiWeather {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL).client(client)
                .build()
            return retrofit.create(ApiWeather::class.java)

        }
    }
}