package com.drag0n.weatherf0recastn3w.domane.API

import com.drag0n.weatherf0recastn3w.data.current.NewCurrent
import com.drag0n.weatherf0recastn3w.data.forecast.NewForecast

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiWeather {
//    @GET("data/2.5/weather?&&units=metric&")
//    suspend fun getWeatherDayNowLocation(
//        @Query("lat") lat: String,
//        @Query("lon") lon: String,
//        @Query("appid") api: String,
//        @Query("lang") lang: String
//    ): Response<WeatherDayNow>
//    @GET("data/2.5/forecast?&&units=metric&")
//    suspend fun getWeatherWeekLocation(
//        @Query("lat") lat: String,
//        @Query("lon") lon: String,
//        @Query("appid") api: String,
//        @Query("lang") lang: String
//    ): Response<WeatherWeek>
//
//    @GET("data/2.5/weather?&units=metric&")
//    suspend fun getWeatherDayNowCity(
//        @Query("q") city: String,
//        @Query("appid") api: String,
//        @Query("lang") lang: String
//    ): Response<WeatherDayNow>
//
//    @GET("data/2.5/forecast?&units=metric&")
//    suspend fun getWeatherWeekCity(
//        @Query("q") city: String,
//        @Query("appid") api: String,
//        @Query("lang") lang: String
//    ): Response<WeatherWeek>
//
//
//    @GET("geo/1.0/reverse?&&limit=1&")
//    suspend fun getGeoNowNew(
//        @Query("lat") lat: String,
//        @Query("lon") lon: String,
//        @Query("appid") api: String
//    ): Response<GetGeoNew>

        @GET("v1/forecast.json?&days=3&&")
    suspend fun getForecast(
            @Query("key") api: String,
            @Query("q") latLon: String,
            @Query("lang") lang: String
    ): Response<NewCurrent>
}