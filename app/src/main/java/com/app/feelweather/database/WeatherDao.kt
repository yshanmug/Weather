package com.app.feelweather.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface WeatherDao {

    @Query("select id from WeatherEntityData order by id desc limit 1 ")
    suspend fun getLatestWeatherID() : Long?

    @Insert
    suspend fun insertWeatherData(weatherData: WeatherEntityData) : Long

    @Insert
    suspend fun insertHourlyData(hourlyData: List<HourlyData>)

    @Insert
    suspend fun insertDailyData(dailyData: List<DailyData>)

    @Insert
    suspend fun insertAirQualityData(airQualityData : List<AirQualityIndex>)

    @Query("delete from Weatherentitydata")
    suspend fun clearWeatherData()


    @Query("select * from HourlyData where weatherDataId = :parentId ")
    suspend fun getHourlyData(parentId: Long) : List<HourlyData>

    @Query("select * from DailyData where weatherDataId = :parentId")
    suspend fun getDailyData(parentId: Long) : List<DailyData>

    @Transaction
    @Query("select * from WeatherEntityData where id= :parentId ")
    suspend fun getEntireWeatherData(parentId: Long?): WeatherWithDetails


    @Insert
    suspend fun insertLocationName(locationName: LocationName)


}


