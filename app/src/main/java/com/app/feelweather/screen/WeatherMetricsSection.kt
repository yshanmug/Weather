package com.app.feelweather.screen


import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.feelweather.R
import com.app.feelweather.model.weatherdata.AirQualityHourlyData
import com.app.feelweather.model.weatherdata.WeatherHourlyData
import java.time.LocalDateTime


//final before
@Composable
fun WeatherMetricsSection(currentWeatherData: Pair<WeatherHourlyData?, AirQualityHourlyData?>) {

    currentWeatherData.let { (hourlyData, airQualityData) ->
        if (hourlyData != null) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
//                    .height(180.dp),  // Increased height for better spacing
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.elevatedCardColors(Color(0xFFF0F0F0)),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(IntrinsicSize.Max),
                ) {
                    // Left Column

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
                            .weight(2f),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        WeatherMetricItem(
                            icon = R.drawable.percipitation,
                            metricName = "Precipitation",
                            value = hourlyData.precipitation.toInt(),
                            unit = "mm",
                            color = Color(0xFF4CAF50)  // Green
                        )

                        WeatherMetricItem(
                            icon = R.drawable.wind_speed,
                            metricName = "Wind",
                            value = (hourlyData.windSpeed * 1.60934).toInt(),
                            unit = "km/h",
                            color = Color(0xFF2196F3)  // Blue
                        )

                        WeatherMetricItem(
                            icon = R.drawable.uv_index,
                            metricName = "UV Index",
                            value = hourlyData.uvIndex.toInt(),
                            unit = "",
                            color = Color(0xFFFF9800)  // Orange
                        )
                    }

                    // Right Column
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = 16.dp, bottom = 16.dp, start = 4.dp, end = 16.dp)
                            .weight(2f),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        WeatherMetricItem(
                            icon = R.drawable.visibility,
                            metricName = "Visibility",
                            value = (hourlyData.visibility * 0.0003048).toInt(),
                            unit = "km",
                            color = Color(0xFF9C27B0)  // Purple
                        )

                        WeatherMetricItem(
                            icon = R.drawable.humidity,
                            metricName = "Humidity",
                            value = hourlyData.humidity.toInt(),
                            unit = "%",
                            color = Color(0xFF03A9F4)  // Light Blue
                        )
                        WeatherMetricItem(
                            icon = R.drawable.air_quality,
                            metricName = "Air Quality",
                            value = airQualityData?.airQualityIndex?.toInt() ?: 0, // Placeholder
                            unit = "",
                            color = Color(0xFF009688)  // Teal
                        )

                    }
                }
            }
        }


    }
}

@Composable
fun WeatherMetricItem(
    @DrawableRes icon: Int,
    metricName: String,
    value: Int,
    unit: String,
    color: Color,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
//        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // Icon with circular background
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(color.copy(alpha = 0.2f), CircleShape)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = metricName,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.size(5.dp))
        Text(
            text = metricName,
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.size(5.dp))
        Text(
            text = "$value $unit",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun WeatherMetricsSectionPreview() {
    val mockHourlyData = WeatherHourlyData(
        time = "",
        timeInISO = LocalDateTime.now(),
        temperatureCelsius = 0.0,
        humidity = 72.0,
        feelsLikeTemperature = 0.0,
        precipitation = 5.2,
        visibility = 100000.0, // ft
        windSpeed = 10.0, // mph
        uvIndex = 3.0f,
        isDay = 1,
        weatherCode = 0,
        weatherType = ""
    )
    val airQualityHourlyData = AirQualityHourlyData(
        time = "",
        airQualityIndex = 0.0
    )
    WeatherMetricsSection(Pair(mockHourlyData, airQualityHourlyData))
}



