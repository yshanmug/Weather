package com.app.feelweather.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.app.feelweather.model.weatherdata.WeatherDailyData
import com.app.feelweather.model.weatherdata.WeatherMapper.Companion.toWeatherInfo
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyForecastSubSection(
    weeklyForecastList: List<WeatherDailyData>, dailyData: WeatherDailyData?,
    onFutureForecastClicked: (WeatherDailyData) -> Unit = {},
) {
    if (dailyData != null) {
        val lazyListState = rememberLazyListState()
        val currentIndex = weeklyForecastList.indexOfFirst {
            it.time == dailyData.time
        }.coerceAtLeast(0)

        LaunchedEffect(dailyData.time) {
            if (weeklyForecastList.isNotEmpty() && currentIndex >= 0) {
                delay(300)
                lazyListState.animateScrollToItem(currentIndex)
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            val localTime = LocalDateTime.now().hour //.hour
            Log.d("localTimee1", localTime.toString())
            val isDay: Boolean = when {
                localTime in 6..17 -> true  // Daytime from 6 AM to 5 PM
                else -> false               // Nighttime otherwise
            }

//    when
            weeklyForecastList.forEach { it ->
                val (dayOfWeek, date) = formatToDayAndDate(it.time, FormatType.DAY_ONLY)
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val weatherInfo =
                    dailyData.weatherCode.toWeatherInfo(isDay = isDay)
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
                            append("${dailyData.maxTemp.toInt()}")
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 24.sp,
                                    baselineShift = BaselineShift.Superscript,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("°C")
                            }
                            append("/")
                            append("${dailyData.minTemp.toInt()}")
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 24.sp,
                                    baselineShift = BaselineShift.Superscript,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("°C")
                            }
                        },
                        fontSize = 70.sp // Smaller than 80.sp to accommodate full text nicely
                    )


                    Text(
                        text = dailyData.weatherType,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.W500
                    )


                }


            }


        }


    }


}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun WeeklyForecastSubSectionPreview() {
    val baseDate = LocalDate.now()
    val selectedDay = { mutableStateOf<WeatherDailyData?>(null) }
    val dailyData = WeatherDailyData(
        time = "2025-07-23",
        weatherCode = 0,
        weatherType = "clear",
        weatherIconResId = R.drawable.clearsky_day,
        minTemp = 9f,
        maxTemp = 17f,
        sunset = "19:90",
        sunrise = "05:50"
    )

    val mockData = List(7) { hour ->
        WeatherDailyData(
            time = baseDate.plusDays(hour.toLong()).toString(),
            weatherCode = 0,
            weatherType = "clear",
            weatherIconResId = R.drawable.clearsky_day,
            sunrise = "19:00",
            sunset = "05:90",
            maxTemp = 17f,
            minTemp = 9f,
        )
    }
    WeeklyForecastSubSection(
        mockData, dailyData,
        onFutureForecastClicked = {
        })
}