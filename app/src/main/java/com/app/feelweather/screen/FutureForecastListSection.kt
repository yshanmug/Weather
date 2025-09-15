package com.app.feelweather.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.feelweather.model.weatherdata.WeatherDailyData
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FutureForecastListSection(
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
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val localTime = LocalDateTime.now().hour //.hour
            Log.d("localTimee1", localTime.toString())
            val isDay: Boolean = when {
                localTime in 6..17 -> true  // Daytime from 6 AM to 5 PM
                else -> false               // Nighttime otherwise
            }

//    when

            LazyRow(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .height(30.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                state = lazyListState
            ) {
                itemsIndexed(weeklyForecastList) { index, day ->

                    val isCurrentDay = index == currentIndex
                    val (dayOfWeek, date) = formatToDayAndDate(day.time, FormatType.DAY_ONLY)
                    Column(
                        modifier = Modifier
                            .clickable { onFutureForecastClicked(day) }
                            .background(
                                shape = RoundedCornerShape(15.dp),
                                color = if (isCurrentDay) Color(0xFF6d6d6d) else Color(0xFFdfdfdf)
                            )  //0xFFdfdfdf
                            .width(70.dp)
                            .height(50.dp),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$dayOfWeek $date",
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}