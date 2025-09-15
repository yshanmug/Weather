package com.app.feelweather

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.feelweather.screen.FutureForecastScreen
import com.app.feelweather.screen.WeatherHomeScreen
import com.app.feelweather.ui.theme.WeatherTheme
import com.app.feelweather.viewmodels.WeatherViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private val weatherViewModel: WeatherViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isFineLocationGranted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val isCoarseLocationGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (isFineLocationGranted || isCoarseLocationGranted) {
                checkGpsAndRequestEnable()

            } else {
                Toast.makeText(this, "Gps is required for weather updates", Toast.LENGTH_SHORT)
                    .show()
                weatherViewModel.getWeatherDataRoomDB()
            }
        }

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )

        setContent {
            WeatherTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "WeatherHomeScreen") {
                    composable("WeatherHomeScreen") {
                        WeatherHomeScreen(
                            weatherViewModel = weatherViewModel,
                            onBackPress = { finish() },
                            onFutureForecastClicked = { weatherDailyData ->
                                weatherViewModel.setClickedDailyWeather(weatherDailyData)
                                weatherViewModel.setClickedHourlyWeather(weatherDailyData.time)
                                navController.navigate("FutureForecastScreen")
                            })
                    }
                    composable("FutureForecastScreen") {
                        FutureForecastScreen(
                            weatherViewModel = weatherViewModel,
                            navController = navController,
                            onBackPress = { navController.navigate("WeatherHomeScreen") },
                            onFutureForecastClicked = { weatherDailyData ->
                                weatherViewModel.setClickedDailyWeather(weatherDailyData)
                                weatherViewModel.setClickedHourlyWeather(weatherDailyData.time)
                            })
                    }
                }

            }
        }
    }


    private fun checkGpsAndRequestEnable() {

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_LOW_POWER, 10000)
            .apply { setMinUpdateIntervalMillis(5000) }.build()

        var locationSettingsRequest =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                .setAlwaysShow(true).setNeedBle(true).build()

        LocationServices.getSettingsClient(this).checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener {
                weatherViewModel.getWeatherData()
            }.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        exception.startResolutionForResult(
                            this, 1001
                        ) // Request code for GPS enable dialog
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Log.e("GPS", "Error enabling GPS", sendEx)
                    }
                }
            }
    }


    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            weatherViewModel.getWeatherData()
        } else {
            weatherViewModel.getWeatherDataRoomDB()
            Toast.makeText(
                this, "Location permissions are needed for the app to work", Toast.LENGTH_SHORT
            ).show()
            Log.d("GPS", "User denied enabling GPS")
        }
    }
}


