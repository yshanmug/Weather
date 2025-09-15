package com.app.feelweather.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.app.feelweather.model.weatherdata.WeatherHourlyData
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyForecastSection(hourlyData: List<WeatherHourlyData>) {

    Log.d("hourlyDATAA", hourlyData.toString())

    val lazyListState = rememberLazyListState()
    val context = LocalContext.current

    val currentHour = LocalDateTime.now().hour

    val currentIndex = hourlyData.indexOfFirst {
        it.timeInISO.hour == currentHour
    }.coerceAtLeast(0)

    LaunchedEffect(hourlyData) {
        if (hourlyData.isNotEmpty()) {
            delay(300)
            val currentIndex = hourlyData.indexOfFirst {
                it.timeInISO.hour == currentHour
            }.coerceAtLeast(0)
            lazyListState.scrollToItem(index = currentIndex, scrollOffset = 0)
        }

    }


    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(170.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.elevatedCardColors(Color(0xFFF0F0F0)),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceAround) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Icon(
                    painter = painterResource(R.drawable.hour_glass),
                    contentDescription = "Hour glass",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Hourly Forecast",
                    fontWeight = FontWeight.Bold
                )
            }



            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(start = 16.dp)
                ,

                horizontalArrangement = Arrangement.spacedBy(10.dp),
                state = lazyListState
            ) {
                item { Spacer(modifier = Modifier.width(8.dp)) }
                itemsIndexed(hourlyData) { index, data ->

                    val isCurrentHour = index == currentIndex
//0xFFBABABA

                    Column(
                        modifier = Modifier
                            .fillMaxHeight(0.80f)
                            .background(
                                shape = RoundedCornerShape(25.dp),
                                color = if (isCurrentHour) Color(0xFF6d6d6d) else Color(0xFFdfdfdf),
                            )
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Icon(
                            painter = painterResource(id = data.weatherIconResId),
                            contentDescription = "Current weather Icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
//                        text =
//                        "${data.temperatureCelsius.toInt()}"
                            text = buildAnnotatedString {
                                append("${data.temperatureCelsius.toInt()}")

                                withStyle(
                                    style = SpanStyle(
                                        baselineShift = BaselineShift.Superscript,
                                    )
                                ) { // smaller degree symbol
                                    append("°")
                                }
                            },
                            fontSize = 13.sp
                        )

                        Text(
                            text = formatTimeUserDevice(context, data.timeInISO),
                            fontSize = 13.sp
                        )


                    }

                }
                item { Spacer(modifier = Modifier.width(8.dp)) }


            }
        }

    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HourlyForecastPreview() {
    val mockData = List(24) { hour ->
        WeatherHourlyData(
            time = "${hour}:00",
            timeInISO = LocalDateTime.now().withHour(hour),
            temperatureCelsius = 20.0,
            humidity = 50.0,
            feelsLikeTemperature = 19.0,
            precipitation = 0.0,
            visibility = 10000.0,
            windSpeed = 5.0,
            uvIndex = 3f,
            isDay = 1,
            weatherCode = 1,
            weatherType = "Clear",
            weatherIconResId = R.drawable.clearsky_day
        )
    }

    HourlyForecastSection(mockData)
}
