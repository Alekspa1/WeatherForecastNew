package com.drag0n.weatherf0recastn3w

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.drag0n.weatherf0recastn3w.Data.WeatherDayNow.WeatherDayNow
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.WeatherWeek

class MainViewModel: ViewModel() {
    val liveDataCurrent = MutableLiveData<WeatherDayNow>()
    val liveDataCurrentWeek = MutableLiveData<WeatherWeek>()
}