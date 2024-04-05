package com.drag0n.weatherf0recastn3w.domane.API

import com.drag0n.weatherf0recastn3w.data.WeatherDayNow.WeatherDayNow
import com.drag0n.weatherf0recastn3w.data.WeatherGetGeo.GetGeoNew
import com.drag0n.weatherf0recastn3w.data.WeatherWeek.WeatherWeek
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiWeather {
    @GET("data/2.5/weather?&&units=metric&")
    suspend fun getWeatherDayNowLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") api: String,
        @Query("lang") lang: String
    ): Response<WeatherDayNow>
    @GET("data/2.5/forecast?&&units=metric&")
    suspend fun getWeatherWeekLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") api: String,
        @Query("lang") lang: String
    ): Response<WeatherWeek>

    @GET("data/2.5/weather?&units=metric&")
    suspend fun getWeatherDayNowCity(
        @Query("q") city: String,
        @Query("appid") api: String,
        @Query("lang") lang: String
    ): Response<WeatherDayNow>

    @GET("data/2.5/forecast?&units=metric&")
    suspend fun getWeatherWeekCity(
        @Query("q") city: String,
        @Query("appid") api: String,
        @Query("lang") lang: String
    ): Response<WeatherWeek>


    @GET("geo/1.0/reverse?&&limit=1&")
    suspend fun getGeoNowNew(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") api: String
    ): Response<GetGeoNew>





    companion object {

        private var BASE_URL = "https://api.openweathermap.org/"
        private val interceptor = HttpLoggingInterceptor()

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