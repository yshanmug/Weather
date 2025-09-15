package com.app.feelweather.model.weatherdata

import java.time.LocalDateTime


data class WeatherHourlyData(
    val time: String,
    val timeInISO: LocalDateTime,
    val temperatureCelsius: Double = 0.0,
    val humidity: Double = 0.0,
    val feelsLikeTemperature: Double = 0.0,
    val precipitation: Double = 0.0,
    val visibility: Double = 0.0,
    val windSpeed: Double = 0.0,
    val uvIndex: Float = 0f,
    val isDay: Int = 0,
    val weatherCode: Int = 0,
    val weatherType: String = "",
    val weatherIconResId: Int = 0,
)

data class WeatherDailyData(
    val time: String = "",
    val weatherCode: Int = 0,
    val weatherType: String = "",
    val weatherIconResId: Int = 0,
    val sunrise: String = "",
    val sunset: String = "",
    val maxTemp: Float = 0f,
    val minTemp: Float = 0f,
    val maxWindSpeed: Float = 0f,
)

data class AirQualityHourlyData(
    val time: String = "",
    val airQualityIndex: Double? = 0.0,
)

data class WeatherData(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val locationName: String = "",
    val hourlyData: List<WeatherHourlyData> = emptyList(),
    val dailyData: List<WeatherDailyData> = emptyList(),
    val airQuality: List<AirQualityHourlyData> = emptyList(),
)

