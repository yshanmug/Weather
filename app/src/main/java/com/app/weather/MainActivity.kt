package com.app.weather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.compose.ui.unit.dp
import com.app.weather.remote.WeatherResponseDto
import com.app.weather.screen.WeatherHomeScreen
import com.app.weather.ui.theme.WeatherTheme
import com.app.weather.viewmodels.WeatherViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import dagger.hilt.android.AndroidEntryPoint
import dagger.internal.InjectedFieldSignature
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var weatherViewModel: WeatherViewModel

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var settingsClient: SettingsClient
//    private lateinit var locationRequest: LocationRequest
    private lateinit var locationSettingsRequest: LocationSettingsRequest



//    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        settingsClient = LocationServices.getSettingsClient(this)
//        createLocationRequest()
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){ permissions ->
            Log.d("printlog", permissions.toString())
            val isFineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val isCoarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
            if (isFineLocationGranted || isCoarseLocationGranted) {
                checkGpsAndRequestEnable()
            }

                weatherViewModel.getWeatherData()

        }
        permissionLauncher.launch(arrayOf(
           Manifest.permission.ACCESS_FINE_LOCATION,
           Manifest.permission.ACCESS_COARSE_LOCATION,
        ))

        setContent {
            WeatherTheme {
                //Testing for commit A
//                val weatherViewModel = hiltViewModel<WeatherViewModel>()
                val weatherState = weatherViewModel.weatherState.collectAsState().value
                //Testing for commit B1
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherContent(
                        weatherState = weatherState,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                //Testing for commit C
            }
        }


    }

//    private fun createLocationRequest() {
//        var locationRequest = LocationRequest.Builder(
//            Priority.PRIORITY_HIGH_ACCURACY,
//            10000 // 10 seconds update interval
//        ).build()
//
//        var locationSettingsRequest = LocationSettingsRequest.Builder()
//            .addLocationRequest(locationRequest)
//            .setAlwaysShow(true) // Show the GPS enable dialog
//            .build()
//    }


    private fun checkGpsAndRequestEnable() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener {
                // GPS is already enabled, proceed with location fetching
                Log.d("GPS", "GPS is already enabled")
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        exception.startResolutionForResult(
                            this,
                            1001
                        ) // Request code for GPS enable dialog
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Log.e("GPS", "Error enabling GPS", sendEx)
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

        //Testing for commit D
    // Display the weather data in a LazyColumn
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        // Header
        item {
            Text(
                text = "Hourly Weather Data.",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp, top = 25.dp)
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
                modifier = Modifier.padding(bottom = 16.dp, top = 30.dp)
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
    WeatherHomeScreen()
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
