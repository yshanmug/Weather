package com.app.feelweather.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class WeatherEntityData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = WeatherEntityData::class,
        parentColumns = ["id"],
        childColumns = ["weatherDataId"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index("weatherDataId"), Index("time")]
)
data class HourlyData(
    val weatherDataId: Long = 0,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val time: String = "",
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

@Entity(
    foreignKeys = [ForeignKey(
        entity = WeatherEntityData::class,
        parentColumns = ["id"],
        childColumns = ["weatherDataId"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index("weatherDataId"), Index("time")]
)
data class DailyData(
    val weatherDataId: Long = 0,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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

@Entity(
    foreignKeys = [ForeignKey(
        entity = WeatherEntityData::class,
        parentColumns = ["id"],
        childColumns = ["weatherDataId"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index("weatherDataId")]
)
data class LocationName(
    val weatherDataId: Long = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val locationName: String = "",
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = WeatherEntityData::class,
        parentColumns = ["id"],
        childColumns = ["weatherDataId"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index("weatherDataId")]
)
data class AirQualityIndex(
    val weatherDataId: Long = 0,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val time: String = "",
    val airQualityIndex: Double? = 0.0,
)


data class WeatherWithDetails(
    @Embedded val weather: WeatherEntityData,

    @Relation(
        parentColumn = "id", entityColumn = "weatherDataId"
    ) val locationName: LocationName,

    @Relation(
        parentColumn = "id", entityColumn = "weatherDataId"
    ) val hourly: List<HourlyData>,

    @Relation(
        parentColumn = "id", entityColumn = "weatherDataId"
    ) val daily: List<DailyData>,

    @Relation(
        parentColumn = "id",
        entityColumn = "weatherDataId",
    ) val hourlyAirIndex: List<AirQualityIndex>,
)
