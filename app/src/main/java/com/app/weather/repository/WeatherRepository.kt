package com.app.weather.repository

import com.app.weather.remote.WeatherResponseDto

interface WeatherRepository {
    suspend fun getWeatherData(lat : Double, long: Double): WeatherResponseDto
}