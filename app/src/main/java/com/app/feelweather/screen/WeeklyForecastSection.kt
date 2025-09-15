package com.app.feelweather.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyForecastSection(
    weeklyForecastList: List<WeatherDailyData>,
    onFutureForecastClicked: (WeatherDailyData) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .size(475.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.elevatedCardColors(Color(0xFFF0F0F0)),
        elevation = CardDefaults.elevatedCardElevation(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.calendar_1),
                    contentDescription = "Weekly Forecast",
                    modifier = Modifier.size(25.dp)
                )
                Text(
                    text = "7 day Forecast",
                    fontWeight = FontWeight.Bold
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            )
            {
                item { Spacer(modifier = Modifier.width(8.dp)) }
                items(weeklyForecastList) { day ->
                    val (dayOfWeek, date) = formatToDayAndDate(day.time, FormatType.DAY_AND_DATE)
                    Log.d("Current Day", day.toString())
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                            .background(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFdfdfdf)
                            )
                            .height(50.dp)
                            .clickable { onFutureForecastClicked(day) },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = dayOfWeek,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = date,
                                fontSize = 11.sp
                            )
                        }


                        Icon(
                            painter = painterResource(id = day.weatherIconResId),
                            contentDescription = "Weather Code",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(30.dp)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_up),
                                contentDescription = "Arrow Up"
                            )
                            Text(
                                text = "${day.maxTemp.toInt()}",
                                fontSize = 13.sp
                            )

                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_down),
                                contentDescription = "Arrow down"
                            )
                            Text(
                                text = "${day.minTemp.toInt()}",
                                fontSize = 13.sp
                            )

                        }

                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        baselineShift = BaselineShift.Superscript,
                                        fontWeight = FontWeight.Normal
                                    )
                                ) {
                                    append("°")
                                }
                                append("C")
                            },
                            fontSize = 13.sp
                        )


                    }


                }


            }

        }


    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun WeeklyForecastSectionPreview() {
    val mockData = List(7) { hour ->
        WeatherDailyData(
            time = "${hour}:00",
            weatherCode = 0,
            weatherType = "clear",
            weatherIconResId = R.drawable.clearsky_day,
            minTemp = 9f,
            maxTemp = 17f,
            sunset = "5:90",
            sunrise = "4:50"
        )
    }
    WeeklyForecastSection(mockData, onFutureForecastClicked = {})
}
