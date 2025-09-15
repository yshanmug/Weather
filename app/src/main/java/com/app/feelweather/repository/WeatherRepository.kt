package com.app.feelweather.repository

import com.app.feelweather.model.weatherdata.AirQualityHourlyData
import com.app.feelweather.model.weatherdata.WeatherDailyData
import com.app.feelweather.model.weatherdata.WeatherData
import com.app.feelweather.model.weatherdata.WeatherHourlyData

interface WeatherRepository {

    //    Retrofit & RoomDB Operation
    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
        locationName: String,
    ): WeatherData

    suspend fun getCachedOrThrow(): WeatherData
    fun getCurrentDailyWeather(dailyData: List<WeatherDailyData>): WeatherDailyData?
    fun getCurrentWeather(
        hourlyData: List<WeatherHourlyData>,
        airQualityHourlyData: List<AirQualityHourlyData>,
    ): Pair<WeatherHourlyData?, AirQualityHourlyData?>

    suspend fun getAir(latitude: Double, longitude: Double)

}