package com.app.weather.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponseDto(
    @Json(name ="latitude") val latitude: String,
    @Json(name= "longitude") val longitude:String,
    @Json(name = "hourly") val hourly: HourlyWeatherResponseDto,
    @Json(name = "daily") val daily: DailyWeatherResponseDto
)

@JsonClass(generateAdapter = true)
data class HourlyWeatherResponseDto(
    @Json(name = "time") val time: List<String>,
    @Json(name = "temperature_2m") val temperature: List<Double>,
    @Json(name = "relative_humidity_2m") val relativeHumidity: List<Double>,
    @Json(name = "apparent_temperature" ) val feelsLikeTemperature: List<Double>,
    @Json(name = "precipitation") val percipitation : List<Double>,
    @Json(name = "weather_code") val weatherCode: List<Int>,
    @Json(name = "visibility") val visibility: List<Double>,
    @Json(name = "wind_speed_10m") val windSpeed: List<Double>,
    @Json(name = "uv_index") val uvIndex: List<Float>
)

@JsonClass(generateAdapter = true)
data class DailyWeatherResponseDto(
    @Json(name = "time") val date: List<String>,
    @Json(name = "sunrise") val sunRise: List<String>,
    @Json(name = "sunset") val sunSet: List<String>
)
