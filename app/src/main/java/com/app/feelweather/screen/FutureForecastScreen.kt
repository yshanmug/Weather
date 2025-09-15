package com.app.feelweather.screen

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.feelweather.R
import com.app.feelweather.model.weatherdata.WeatherDailyData
import com.app.feelweather.viewmodels.WeatherViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FutureForecastScreen(
    weatherViewModel: WeatherViewModel,
    navController: NavController,
    onBackPress: () -> Unit,
    onFutureForecastClicked: (WeatherDailyData) -> Unit,
) {
    BackHandler {
        navController.navigate("WeatherHomeScreen")
    }
    val weatherState = weatherViewModel.weatherData.collectAsState().value
    val locationName = weatherState.locationName
    val dailyDataList = weatherState.dailyData
    val dailyData = weatherViewModel.selectedDailyWeather.collectAsState().value
    val hourlyData = weatherViewModel.selectedHourlyWeather.collectAsState().value
    val averageHumidity = weatherViewModel.averageHumidity.collectAsState().value
    val maxWindSpeed = dailyData?.maxWindSpeed
    val precipitationSum = weatherViewModel.precipitationSum.collectAsState().value
    val maxUVIndex = weatherViewModel.maxUvIndex.collectAsState().value


    hourlyData.forEach {
        it
        Log.d("ForHourlyData", "$it")
    }

    Log.d("hourlyDataofDaily", hourlyData.toString())
    val isFromFutureScreen: Boolean = true

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.white1_bgimg),
            contentDescription = "Rainy Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
        )
        LazyColumn(
            modifier = Modifier.padding(top = 50.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { TopAppBarSection(isDay = null, locationName, onBackPress, isFromFutureScreen) }
            item { FutureForecastListSection(dailyDataList, dailyData, onFutureForecastClicked) }
            item { WeeklyForecastSubSection(dailyDataList, dailyData, onFutureForecastClicked) }
            item { HourlyForecastSection(hourlyData) }
            item { SunriseSunsetSection(dailyData) }
            item { DailyStatsSection(averageHumidity, maxUVIndex, precipitationSum, maxWindSpeed) }
        }
    }
}


@Composable
@Preview
fun FutureForecastScreenPreview() {

}