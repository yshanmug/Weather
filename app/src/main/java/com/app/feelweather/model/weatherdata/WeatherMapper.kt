package com.app.feelweather.model.weatherdata

import android.os.Build
import androidx.annotation.RequiresApi
import com.app.feelweather.R
import com.app.feelweather.database.AirQualityIndex
import com.app.feelweather.database.DailyData
import com.app.feelweather.database.HourlyData
import com.app.feelweather.database.WeatherEntityData
import com.app.feelweather.database.WeatherWithDetails
import com.app.feelweather.model.weatherdata.WeatherMapper.Companion.toWeatherInfo
import com.app.feelweather.remote.WeatherQualityResponseDto
import com.app.feelweather.remote.WeatherResponseDto
import java.time.LocalDateTime


//Mapper class for DTO to Entity

fun WeatherResponseDto.toWeatherEntity(): WeatherEntityData {
    return WeatherEntityData(
        latitude = latitude.toDouble(),
        longitude = longitude.toDouble()
    )
}

fun WeatherResponseDto.toHourlyEntity(parentId: Long): List<HourlyData> {
    return hourly.time.mapIndexed { index, time ->
        HourlyData(
            weatherDataId = parentId,
            time = time,
            temperatureCelsius = hourly.temperature[index],
            humidity = hourly.relativeHumidity[index],
            feelsLikeTemperature = hourly.feelsLikeTemperature[index],
            precipitation = hourly.precipitation[index],
            visibility = hourly.visibility[index],
            windSpeed = hourly.windSpeed[index],
            uvIndex = hourly.uvIndex[index],
            isDay = hourly.isDay[index],
            weatherCode = hourly.weatherCode[index],
            weatherType = hourly.weatherCode[index].toWeatherInfo().weatherType,
            weatherIconResId = hourly.weatherCode[index].toWeatherInfo().iconResId
        )
    }
}


fun WeatherResponseDto.toDailyEntity(parentId: Long): List<DailyData> {
    return daily.date.mapIndexed { index, time ->
        DailyData(
            weatherDataId = parentId,
            time = time,
            weatherCode = daily.dailyWeatherCode[index],
            weatherType = daily.dailyWeatherCode[index].toWeatherInfo().weatherType,
            weatherIconResId = daily.dailyWeatherCode[index].toWeatherInfo().iconResId,
            sunrise = daily.sunRise[index],
            sunset = daily.sunSet[index],
            maxTemp = daily.maxTemp[index],
            minTemp = daily.minTemp[index],
            maxWindSpeed = daily.maxWindSpeed[index]
        )
    }
}

fun WeatherQualityResponseDto.toHourlyUsAqiEntity(parentId: Long): List<AirQualityIndex> {
    return hourly.time.mapIndexed { index, time ->
        AirQualityIndex(
            weatherDataId = parentId,
            time = time,
            airQualityIndex = hourly.airIndex[index]
        )
    }
}

//Mapper class for Entity to Domain

@RequiresApi(Build.VERSION_CODES.O)
fun WeatherWithDetails.toDomain(): WeatherData {
    return WeatherData(
        latitude = weather.latitude,
        longitude = weather.longitude,
        locationName = locationName.locationName,
        hourlyData = hourly.map { it.toDomain() },
        dailyData = daily.map { it.toDomain() },
        airQuality = hourlyAirIndex.map { it.toDomain() }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun HourlyData.toDomain(): WeatherHourlyData {
    return WeatherHourlyData(
        time = this.time,
        timeInISO = LocalDateTime.parse(this.time),
        temperatureCelsius = this.temperatureCelsius,
        humidity = this.humidity,
        feelsLikeTemperature = this.feelsLikeTemperature,
        precipitation = this.precipitation,
        visibility = this.visibility,
        windSpeed = this.windSpeed,
        uvIndex = this.uvIndex,
        isDay = this.isDay,
        weatherCode = this.weatherCode,
        weatherType = this.weatherType,
        weatherIconResId = this.weatherIconResId
    )
}

fun DailyData.toDomain(): WeatherDailyData {
    return WeatherDailyData(
        time = this.time,
        weatherCode = this.weatherCode,
        weatherType = this.weatherType,
        weatherIconResId = this.weatherIconResId,
        sunrise = this.sunrise,
        sunset = this.sunset,
        maxTemp = this.maxTemp,
        minTemp = this.minTemp,
        maxWindSpeed = this.maxWindSpeed
    )
}

fun AirQualityIndex.toDomain(): AirQualityHourlyData {
    return AirQualityHourlyData(
        time = this.time,
        airQualityIndex = this.airQualityIndex
    )
}


sealed interface WeatherMapper {
    data class WeatherInfo(
        val weatherType: String,
        val iconResId: Int,
    )

    companion object {
        fun Int.toWeatherInfo(isDay: Boolean = true): WeatherInfo =
            when (this) {
                0 -> WeatherInfo(
                    "ClearSky",
                    if (isDay) R.drawable.clearsky_day else R.drawable.clearsky_night
                )

                1 -> WeatherInfo(
                    "MainlyClear",
                    if (isDay) R.drawable.mainly_clear_day else R.drawable.mainly_clear_night
                )

                2 -> WeatherInfo(
                    "PartlyCloudy",
                    if (isDay) R.drawable.partly_cloudy_day else R.drawable.partly_cloudy_night
                )

                3 -> WeatherInfo("Cloudy", R.drawable.mostly_cloudy_day)

                45, 48 -> WeatherInfo("Foggy", R.drawable.foggy)

                51, 53, 55 -> WeatherInfo("Drizzle", R.drawable.drizzle_lmd)
                56, 57 -> WeatherInfo("FreezingDrizzle", R.drawable.freezing_drizzle_ld)

                61 -> WeatherInfo("LightRain", R.drawable.slight_rain)
                63 -> WeatherInfo("Moderate Rain", R.drawable.moderate_rain)
                65, 82 -> WeatherInfo("HeavyRain", R.drawable.heavy_rain)

                66, 67 -> WeatherInfo("FreezingRain", R.drawable.freezing_drizzle_ld)

                71 -> WeatherInfo("SlightSnowFall", R.drawable.slight_snowfall)
                73, 75, 86 -> WeatherInfo("HeavySnowFall", R.drawable.heavy_snowfall)
                77 -> WeatherInfo("SnowGrains", R.drawable.snowgrains)

                80, 81 -> WeatherInfo(
                    "RainShowers",
                    if (isDay) R.drawable.scattered_showers_sm_day else R.drawable.scattered_showers_sm_night
                )

                85 -> WeatherInfo(
                    "SlightSnowShowers",
                    if (isDay) R.drawable.slight_snow_showers_day else R.drawable.slight_snow_showers_night
                )

                95, 96 -> WeatherInfo("Thunderstorm", R.drawable.moderate_slight_thunderstorms)
                99 -> WeatherInfo("HeavyThunderstorm", R.drawable.heavy_hail_thunderstorms)

                else -> WeatherInfo(
                    "ClearSky",
                    if (isDay) R.drawable.clearsky_day else R.drawable.clearsky_night
                )
            }
    }
}






