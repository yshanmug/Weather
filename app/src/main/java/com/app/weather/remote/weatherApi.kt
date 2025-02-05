package com.app.weather.remote

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherApi {
    @Headers("Content-Type: application/json")
//    https://api.open-meteo.com/v1/forecast?latitude=43.46&longitude=-80.52&hourly=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,visibility,wind_speed_10m,uv_index&daily=sunrise,sunset
    @GET("v1/forecast?&hourly=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,visibility,wind_speed_10m,uv_index&daily=sunrise,sunset")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): WeatherResponseDto
}