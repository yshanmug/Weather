package com.app.feelweather.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [WeatherEntityData::class, HourlyData::class, DailyData::class, LocationName::class, AirQualityIndex::class],
    version = 15
)

abstract class WeatherDataBase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        const val DATABASE_NAME = "weather_database"
    }
}

