package com.app.feelweather.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import com.app.feelweather.database.LocationName
import com.app.feelweather.database.WeatherDao
import com.app.feelweather.model.weatherdata.AirQualityHourlyData
import com.app.feelweather.model.weatherdata.WeatherDailyData
import com.app.feelweather.model.weatherdata.WeatherData
import com.app.feelweather.model.weatherdata.WeatherHourlyData
import com.app.feelweather.model.weatherdata.toDailyEntity
import com.app.feelweather.model.weatherdata.toDomain
import com.app.feelweather.model.weatherdata.toHourlyEntity
import com.app.feelweather.model.weatherdata.toHourlyUsAqiEntity
import com.app.feelweather.model.weatherdata.toWeatherEntity
import com.app.feelweather.remote.WeatherApi
import com.app.feelweather.remote.WeatherQualityApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class WeatherRepositoryImp @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherQualityApi: WeatherQualityApi,
    private val weatherDao: WeatherDao,
) : WeatherRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
        locationName: String,
    ): WeatherData {
        return try {
            coroutineScope {
                val weatherCall = async { weatherApi.getWeather(latitude, longitude) }
                val airQualityCall = async { weatherQualityApi.getAirQuality(latitude, longitude) }
                val result = weatherCall.await()
                val airQualityResult = airQualityCall.await()
                if (result.latitude != "0.0" && result.longitude != "0.0" && airQualityResult.longitude != "0.0" && airQualityResult.latitude != "0.0") {
//          #clear old data
                    weatherDao.clearWeatherData()
//          #push new data
                    val weatherEntity = result.toWeatherEntity()
                    val parentId = weatherDao.insertWeatherData(weatherEntity)
                    weatherDao.insertLocationName(
                        LocationName(
                            weatherDataId = parentId, locationName = locationName
                        )
                    )
                    weatherDao.insertHourlyData(result.toHourlyEntity(parentId))
                    weatherDao.insertDailyData(result.toDailyEntity(parentId))
                    weatherDao.insertAirQualityData(airQualityResult.toHourlyUsAqiEntity(parentId))
                }
                getCachedOrThrow()
            }
        } catch (e: IOException) {
            Log.e("Repository", "No internet. Loading from cache ${e.localizedMessage}")
            getCachedOrThrow() // On no internet, load from Room
        } catch (e: Exception) {
            Log.e("Repository", "Unknown error: ${e.localizedMessage}")
            getCachedOrThrow() // On other errors, still load from Room
        }
    }

    override suspend fun getAir(latitude: Double, longitude: Double) {
        val responseValue = weatherQualityApi.getAirQuality(latitude, longitude)
        Log.d("CurrentThreadRepo", "${Thread.currentThread()}")
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getCachedOrThrow(): WeatherData {
        val parentId = weatherDao.getLatestWeatherID()
        Log.d("parentId", parentId.toString())
        return parentId?.let {
            val weatherEntity = weatherDao.getEntireWeatherData(parentId)
            if (weatherEntity.hourly.isNotEmpty() && weatherEntity.daily.isNotEmpty() && weatherEntity.hourlyAirIndex.isNotEmpty()) {
                weatherEntity.toDomain()
            } else {
                throw Exception("Incomplete cached data")
            }
        } ?: throw Exception("No cached data found")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCurrentWeather(
        hourlyData: List<WeatherHourlyData>,
        airQualityHourlyData: List<AirQualityHourlyData>,
    ): Pair<WeatherHourlyData?, AirQualityHourlyData?> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm") // Matches API string
        val now = LocalDateTime.now()
        val targetTime = when {
            now.minute < 35 -> now.withMinute(0)
            now.hour == 23 -> now.plusDays(1).withHour(0).withMinute(0)
            else -> now.plusHours(1).withMinute(0)
        }.withSecond(0).withNano(0)

        val targetTimeString = targetTime.format(formatter)
        Log.d("target time , targettimestring", "$targetTime $targetTimeString")
        val hourlyData = hourlyData.find { it.time == targetTimeString }
        val hourlyAirIndexData = airQualityHourlyData.find { it.time == targetTimeString }
        Log.d(
            "Current Hourly Weather", "${hourlyData.toString()} ${hourlyAirIndexData.toString()} "
        )
        return Pair(hourlyData, hourlyAirIndexData)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCurrentDailyWeather(dailyData: List<WeatherDailyData>): WeatherDailyData? {
        val todayDate = LocalDate.now().toString()
        Log.d("todayDate", todayDate.toString())
        val dailyData = dailyData.find { it.time == todayDate }
        Log.d("ReturnDailyData", dailyData.toString())
        return dailyData
    }
}

