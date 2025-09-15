package com.app.feelweather.screen

import android.content.Context
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.text.format.DateFormat
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.app.feelweather.R
import com.app.feelweather.model.weatherdata.WeatherDailyData
import com.app.feelweather.viewmodels.WeatherViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherHomeScreen(
    weatherViewModel: WeatherViewModel,
    onBackPress: () -> Unit,
    onFutureForecastClicked: (WeatherDailyData) -> Unit,
) {

    val weatherState by weatherViewModel.weatherData.collectAsStateWithLifecycle()
    val isLoading by weatherViewModel.isLoading.collectAsState()
    val errorMessage by weatherViewModel.errorMessage.collectAsState()
    val locationName = weatherState.locationName
    val hourlyDataList = weatherState.hourlyData
    val dailyDataList = weatherState.dailyData
    val currentHourWeatherData by weatherViewModel.currentWeather.collectAsState()
    val hourlyData = currentHourWeatherData.first

    val todayDailyData = weatherViewModel.dailyWeather.collectAsState().value
    val todayHourlyData = hourlyDataList.filter {
        it.timeInISO.toLocalDate() == LocalDateTime.now().toLocalDate()
    }


    BackHandler {
        onBackPress.invoke()
    }


    val isDay = hourlyData?.isDay
    val backgroundImage = if (isDay == 1) {
        painterResource(R.drawable.sunny_bg_img)
    } else {
        painterResource(R.drawable.night_bgimg)
    }

    if (isLoading) {

        LoadingScreen()
    } else if (errorMessage != null) {
        ErrorScreen(message = errorMessage!!)
    } else if (currentHourWeatherData != null) {
        // Your main content
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = backgroundImage,
                contentDescription = "null",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            LazyColumn(
                modifier = Modifier.padding(top = 50.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    TopAppBarSection(
                        currentHourWeatherData.first?.isDay, locationName, onBackPress, false
                    )
                }
                item { CurrentWeatherSection(currentHourWeatherData, todayDailyData) }
                item { WeatherMetricsSection(currentHourWeatherData) }
                item { HourlyForecastSection(todayHourlyData) }
                item { SunriseSunsetSection(todayDailyData) }
                item { WeeklyForecastSection(dailyDataList, onFutureForecastClicked) }
                item { Spacer(modifier = Modifier.size(50.dp)) }
            }
        }
    } else {
        ErrorScreen(message = "No weather data available")
    }

}

@Composable
fun LoadingScreen() {
    val context = LocalContext.current
    val gifEnabledLoader = ImageLoader.Builder(context).components {
        if (SDK_INT >= 28) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(R.drawable.weatherloading).crossfade(true)
                .build(),
            imageLoader = gifEnabledLoader,
            contentDescription = "Animated GIF",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight

        )
    }
}


@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = Color.Red)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun formatTimeUserDevice(context: Context, localTime: LocalDateTime): String {
    val is24HourFormat = DateFormat.is24HourFormat(context)
    val formatter = if (is24HourFormat) {
        DateTimeFormatter.ofPattern("HH:mm")
    } else {
        DateTimeFormatter.ofPattern("h a")
    }
    return localTime.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimeBasedOnSystem(context: Context, timeString: String?): String {
    if (timeString.isNullOrBlank()) return ""

    return try {
        val localDateTime = LocalDateTime.parse(timeString)

        // Check system time format
        val is24Hour = DateFormat.is24HourFormat(context)

        val formatter = if (is24Hour) {
            DateTimeFormatter.ofPattern("HH:mm")
        } else {
            DateTimeFormatter.ofPattern("h:mm a") // e.g., 5:45 AM
        }

        localDateTime.format(formatter)
    } catch (e: Exception) {
        ""
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun formatToDayAndDate(dateString: String, formatType: FormatType): Pair<String, String> {
    val todayDate = LocalDate.now()
    val date = LocalDate.parse(dateString)
    Log.d("date inside", date.toString())
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()) // "Mon"

    return when (formatType) {
        FormatType.DAY_ONLY -> {
            val dayNumber = date.dayOfMonth.toString()
            when {
                date.isEqual(todayDate) -> Pair("Today", dayNumber)
                else -> Pair(dayOfWeek, dayNumber)
            }
        }

        FormatType.DAY_AND_DATE -> {
            val formattedDate = date.format(DateTimeFormatter.ofPattern("MM/dd")) // "06/05"
            when {
                date.isEqual(todayDate) -> Pair("Today", formattedDate)
                else -> Pair(dayOfWeek, formattedDate)
            }

        }

    }
}


enum class FormatType {
    DAY_ONLY, DAY_AND_DATE
}
