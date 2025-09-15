package com.app.feelweather.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.feelweather.R
import com.app.feelweather.model.weatherdata.AirQualityHourlyData
import com.app.feelweather.model.weatherdata.WeatherDailyData
import com.app.feelweather.model.weatherdata.WeatherHourlyData
import com.app.feelweather.model.weatherdata.WeatherMapper.Companion.toWeatherInfo
import java.time.LocalDateTime

@Composable
fun CurrentWeatherSection(
    currentWeatherData: Pair<WeatherHourlyData?, AirQualityHourlyData?>,
    todayDailyData: WeatherDailyData?,
) {
    val (hourlyData, airIndexData) = currentWeatherData

    if (hourlyData != null && airIndexData != null && todayDailyData != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.75f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val weatherInfo =
                    hourlyData.weatherCode.toWeatherInfo(isDay = hourlyData.isDay == 1)
                val iconRes = weatherInfo.iconResId
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = weatherInfo.weatherType,
                    modifier = Modifier
                        .size(110.dp)
                )


                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {

                    Text(
                        text = buildAnnotatedString {
                            append("${hourlyData.temperatureCelsius.toInt()}")

                            withStyle(
                                style = SpanStyle(
                                    fontSize = 24.sp,
                                    baselineShift = BaselineShift.Superscript,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                                        Color.White
                                    } else {
                                        Color.Black
                                    }
                                )
                            ) { // smaller degree symbol
                                append("°C")
                            }
                        },
                        fontSize = 80.sp,
                        color = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )

                    Text(
                        text = hourlyData.weatherType,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.W500,
                        color = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )


                }


            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.PlayArrow,
                            contentDescription = "Up Arrow",
                            modifier = Modifier.rotate(-90f),
                            tint = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                                Color.White
                            } else {
                                Color.Black
                            }
                        )

                        Text(
                            text = buildAnnotatedString {
                                append("${todayDailyData.maxTemp.toInt()}")
                                withStyle(
                                    style = SpanStyle(

                                        baselineShift = BaselineShift.Superscript,
                                        fontWeight = FontWeight.Normal,
                                        color = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                                            Color.White
                                        } else {
                                            Color.Black
                                        }
                                    )
                                ) {
                                    append("°")
                                }

                            },
                            fontWeight = FontWeight.W500,
                            color = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                                Color.White
                            } else {
                                Color.Black
                            }

                        )
                    }
                    Text(
                        text = "High",
                        fontWeight = FontWeight.W500,
                        color = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.feels_like_temp),
                            contentDescription = "feels like temperature",
                            modifier = Modifier.size(24.dp),
                            tint = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                                Color.White
                            } else {
                                Color.Black
                            }
                        )

                        Text(
                            text = buildAnnotatedString {
                                append("${hourlyData.feelsLikeTemperature.toInt()}")
                                withStyle(
                                    style = SpanStyle(

                                        baselineShift = BaselineShift.Superscript,
                                        fontWeight = FontWeight.Normal,
                                        color = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                                            Color.White
                                        } else {
                                            Color.Black
                                        }
                                    )
                                ) {
                                    append("°")
                                }

                            },
                            fontWeight = FontWeight.W500,
                            color = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                                Color.White
                            } else {
                                Color.Black
                            }
                        )
                    }
                    Text(
                        text = "Feels Like",
                        fontWeight = FontWeight.W500,
                        color = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {

                        Icon(
                            imageVector = Icons.TwoTone.PlayArrow,
                            contentDescription = "Down Arrow",
                            modifier = Modifier.rotate(90f),
                            tint = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                                Color.White
                            } else {
                                Color.Black
                            }
                        )

                        Text(
                            text = buildAnnotatedString {
                                append("${todayDailyData.minTemp.toInt()}")
                                withStyle(
                                    style = SpanStyle(

                                        baselineShift = BaselineShift.Superscript,
                                        fontWeight = FontWeight.Normal,
                                        color = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                                            Color.White
                                        } else {
                                            Color.Black
                                        }
                                    )
                                ) {
                                    append("°")
                                }

                            },
                            fontWeight = FontWeight.W500,
                            color = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                                Color.White
                            } else {
                                Color.Black
                            }
                        )
                    }
                    Text(
                        text = "Low",
                        fontWeight = FontWeight.W500,
                        color = if (isSystemInDarkTheme() || hourlyData.isDay == 0) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )
                }

            }
        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CurrentWeatherSectionPreview() {
    val mockHourlyData = WeatherHourlyData(
        time = "",
        timeInISO = LocalDateTime.now(),
        temperatureCelsius = 0.0,
        humidity = 0.0,
        feelsLikeTemperature = 0.0,
        precipitation = 0.0,
        visibility = 0.0,
        windSpeed = 0.0,
        uvIndex = 0f,
        isDay = 0,
        weatherCode = 0,
        weatherType = ""
    )

    val airQualityMockData = AirQualityHourlyData(
        time = "",
        airQualityIndex = 0.0
    )

    val mockDailyData = WeatherDailyData(
        time = "",
        weatherType = "",
        weatherCode = 0,
        sunrise = "",
        sunset = "",
        maxTemp = 0f,
        minTemp = 0f

    )
    CurrentWeatherSection(Pair(mockHourlyData,airQualityMockData), mockDailyData)
}

