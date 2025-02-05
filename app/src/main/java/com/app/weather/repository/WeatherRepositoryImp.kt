package com.app.weather.repository

import com.app.weather.remote.WeatherApi
import com.app.weather.remote.WeatherResponseDto
import javax.inject.Inject

class WeatherRepositoryImp @Inject constructor(private val weatherApi: WeatherApi): WeatherRepository {

    override suspend fun getWeatherData(lat : Double, long: Double) : WeatherResponseDto{
        return weatherApi.getWeather(lat, long)

    }


}