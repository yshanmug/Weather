package com.app.feelweather.screen

//import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.feelweather.R


@Composable
fun DailyStatsSection(
    avgHumidity: Float,
    maxUvIndex: Float,
    precipitation: Double,
    maxWindSpeed: Float?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(180.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Humidity Card
            item {
                ElevatedCard(
                    modifier = Modifier,
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.humidity),
                                contentDescription = "Avg Humidity for the day",
                                modifier = Modifier.size(20.dp)
                            )
                            Text("Humidity", fontWeight = FontWeight.Bold)

                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            buildAnnotatedString {
                                append("Avg Humidity level ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("${avgHumidity.toInt()}%")
                                }
                            },
                            fontSize = 14.sp
                        )
                    }
                }
            }
            // UV Index Card
            item {
                ElevatedCard(
                    modifier = Modifier,
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.uv_index),
                                contentDescription = "UV Index",
                                modifier = Modifier.size(20.dp)
                            )
                            Text("UV Index", fontWeight = FontWeight.Bold)

                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            buildAnnotatedString {
                                append("Highest UV exposure ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("${maxUvIndex.toInt()}")
                                }
                            },
                            fontSize = 14.sp
                        )

                    }
                }
            }
            // Precipitation Card
            item {
                ElevatedCard(
                    modifier = Modifier,
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.percipitation),
                                contentDescription = "Precipitation",
                                modifier = Modifier.size(20.dp)
                            )
                            Text("Precipitation", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            buildAnnotatedString {
                                append("Precipitation Sum ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("$precipitation mm")
                                }
                            },
                            fontSize = 14.sp
                        )

                    }
                }
            }
            // Wind Speed Card
            item {
                ElevatedCard(
                    modifier = Modifier,
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.wind_speed),
                                contentDescription = "Wind Speed",
                                modifier = Modifier.size(20.dp)
                            )
                            Text("Wind Speed", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            buildAnnotatedString {
                                append("Max wind speed ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("${maxWindSpeed?.toInt()} km/h")
                                }
                            },
                            fontSize = 14.sp
                        )

                    }
                }
            }
        }
    }

}


@Composable
@Preview
fun DailyStatsSectionPreview() {
    DailyStatsSection(8f, 8f, 0.5, 85.5f)
}
