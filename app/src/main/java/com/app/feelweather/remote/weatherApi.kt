package com.app.feelweather.remote

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherApi {
    @Headers("Content-Type: application/json")
//    https://api.open-meteo.com/v1/forecast?latitude=43.46&longitude=-80.52&hourly=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,visibility,wind_speed_10m,uv_index,is_day&wind_speed_unit=mph&precipitation_unit=inch&daily=sunrise,sunset,temperature_2m_max,temperature_2m_min,wind_speed_10m_max
//    &minutely_15=is_day,temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,visibility,wind_speed_10m&timezone=auto
    @GET("v1/forecast?hourly=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,visibility,wind_speed_10m,uv_index,is_day&wind_speed_unit=mph&precipitation_unit=inch&daily=weather_code,sunrise,sunset,temperature_2m_max,temperature_2m_min,wind_speed_10m_max&timezone=auto&models=best_match")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): WeatherResponseDto
}