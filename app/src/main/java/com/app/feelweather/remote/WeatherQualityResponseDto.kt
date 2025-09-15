package com.app.feelweather.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherQualityResponseDto(
    @Json(name ="latitude") val latitude: String,
    @Json(name= "longitude") val longitude:String,
    @Json(name = "hourly") val hourly: HourlyDataResponseDto,
)

@JsonClass(generateAdapter = true)
data class HourlyDataResponseDto(
    @Json(name = "time") val time: List<String>,
    @Json(name = "us_aqi") val airIndex: List<Double>
)