package com.drag0n.weatherf0recastn3w.domane.API

import com.drag0n.weatherf0recastn3w.data.astrology.NewAstrology
import com.drag0n.weatherf0recastn3w.data.current.NewCurrent
import com.drag0n.weatherf0recastn3w.data.forecast.NewForecast

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiWeather {

        @GET("v1/current.json?&&&")
    suspend fun getCurrent(
            @Query("key") api: String,
            @Query("q") latLon: String,
            @Query("lang") lang: String
    ): Response<NewCurrent>

    @GET("v1/forecast.json?&days=3&&alerts=yes")
    suspend fun getForecast(
        @Query("key") api: String,
        @Query("q") latLon: String,
        @Query("lang") lang: String
    ): Response<NewForecast>

    @GET("v1/astronomy.json?&days=3&&")
    suspend fun getAstronomy(
        @Query("key") api: String,
        @Query("q") latLon: String,
        @Query("lang") lang: String
    ): Response<NewAstrology>
}