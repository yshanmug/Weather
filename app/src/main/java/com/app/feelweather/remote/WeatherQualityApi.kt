package com.app.feelweather.remote

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


//https://air-quality-api.open-meteo.com/v1/air-quality?latitude=43.653076,&longitude=-79.386242&hourly=us_aqi&timezone=auto
interface WeatherQualityApi {
    @Headers("Content-Type: application/json")
    @GET("v1/air-quality?hourly=us_aqi&timezone=auto")
    suspend fun getAirQuality(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): WeatherQualityResponseDto
}
