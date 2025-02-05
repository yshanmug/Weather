package com.app.weather

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.weather.remote.WeatherResponseDto
import com.app.weather.ui.theme.WeatherTheme
import com.app.weather.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTheme {
                val weatherViewModel = hiltViewModel<WeatherViewModel>()
                val weatherState = weatherViewModel.weatherState.collectAsState().value

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherContent(
                        weatherState = weatherState,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherContent(weatherState: WeatherResponseDto?, modifier: Modifier = Modifier) {
    if (weatherState != null) {
        val latitude = weatherState.latitude
        val longitude = weatherState.longitude
    val hourlyData = weatherState.hourly
    val dailyData = weatherState.daily

    // Display the weather data in a LazyColumn
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        // Header
        item {
            Text(
                text = "Hourly Weather Data.",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp, top = 20.dp)
            )

        }
        item {
            Text(
                text = "Hourly Weather Data.",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp, top = 20.dp)
            )

        }
        item {
            Text(
                text = "Hourly Weather Data.",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp, top = 20.dp)
            )

        }
        item{
            Text(
                text = "Latitude: ${weatherState.latitude}"
            )
            Text(text = "Longitude: ${weatherState.longitude}",)
        }

        // Hourly weather data
        items(hourlyData.time.size) { index ->
            HourlyWeatherRow(
                time = hourlyData.time[index],
                temperature = hourlyData.temperature[index],
                humidity = hourlyData.relativeHumidity[index],
                windSpeed = hourlyData.windSpeed[index],
                feelsLikeTemp = hourlyData.feelsLikeTemperature[index],
                percipitation = hourlyData.percipitation[index],
                weatherCode = hourlyData.weatherCode[index],
                visibility = hourlyData.visibility[index],
                uvIndex = hourlyData.uvIndex[index]

            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Daily Weather Data",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        items(dailyData.date.size) { index ->
            DailyWeatherRow(
                date = dailyData.date[index],
                sunrise = dailyData.sunRise[index],
                sunset = dailyData.sunSet[index]
            )
        }
    }
    } else {
        Text(
            text = "Loading weather data...",
            modifier = modifier
        )
    }
}

@Composable
fun HourlyWeatherRow(time: String, temperature: Double, humidity: Double, windSpeed: Double, feelsLikeTemp: Double,
                     percipitation: Double,
                     weatherCode: Int,
                     visibility: Double,
                     uvIndex : Float ){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = "Time: $time", modifier = Modifier)
        Text(text = "Temp: ${temperature}°C", modifier = Modifier)
        Text(text = "Humidity: ${humidity}%", modifier = Modifier)
        Text(text = "Wind: ${windSpeed}m/s", modifier = Modifier)
        Text(text = "feelsLikeTemp: ${feelsLikeTemp}m/s", modifier = Modifier)
        Text(text = "percipitation: ${percipitation}m/s", modifier = Modifier)
        Text(text = "weatherCode: ${weatherCode}m/s", modifier = Modifier)
        Text(text = "visibility: ${visibility}m/s", modifier = Modifier)
        Text(text = "uvIndex: ${uvIndex}m/s", modifier = Modifier)

    }
}


@Composable
fun DailyWeatherRow(date: String, sunrise: String, sunset: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Date: $date", modifier = Modifier.weight(1f))
        Text(text = "Sunrise: $sunrise", modifier = Modifier.weight(1f))
        Text(text = "Sunset: $sunset", modifier = Modifier.weight(1f))
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    WeatherTheme {
//        Greeting("Android")
//    }
//}