package com.drag0n.weatherf0recastn3w.data.forecast

data class NewForecast(
    val alerts: Alerts,
    val current: Current,
    val forecast: Forecast,
    val location: Location
)