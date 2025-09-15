package com.app.feelweather.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.feelweather.location.LocationTracker
import com.app.feelweather.model.weatherdata.AirQualityHourlyData
import com.app.feelweather.model.weatherdata.WeatherDailyData
import com.app.feelweather.model.weatherdata.WeatherData
import com.app.feelweather.model.weatherdata.WeatherHourlyData
import com.app.feelweather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.math.RoundingMode
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker,
) : ViewModel() {


    private val _locationName = MutableStateFlow<String>("")
    val locationName = _locationName.asStateFlow()

    private val _weatherData = MutableStateFlow(WeatherData())
    var weatherData: StateFlow<WeatherData> = _weatherData.asStateFlow()


    private val _currentWeather =
        MutableStateFlow<Pair<WeatherHourlyData?, AirQualityHourlyData?>>(Pair(null, null))

    var currentWeather: StateFlow<Pair<WeatherHourlyData?, AirQualityHourlyData?>> =
        _currentWeather.asStateFlow()

    private val _dailyWeather = MutableStateFlow<WeatherDailyData?>(null)
    var dailyWeather: StateFlow<WeatherDailyData?> = _dailyWeather.asStateFlow()

    private val _selectedHourlyWeather = MutableStateFlow<List<WeatherHourlyData>>(emptyList())
    val selectedHourlyWeather: StateFlow<List<WeatherHourlyData>> =
        _selectedHourlyWeather.asStateFlow()

    private val _selectedDailyWeather = MutableStateFlow<WeatherDailyData?>(null)
    val selectedDailyWeather: StateFlow<WeatherDailyData?> = _selectedDailyWeather.asStateFlow()

    private val _averageHumidity = MutableStateFlow<Float>(0f)
    val averageHumidity = _averageHumidity.asStateFlow()

    private val _maxUvIndex = MutableStateFlow<Float>(0f)
    val maxUvIndex = _maxUvIndex.asStateFlow()

    private val _precipitationSum = MutableStateFlow<Double>(0.0)
    val precipitationSum = _precipitationSum.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    // error state
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()


    fun calculateFutureMetrics(today: List<WeatherHourlyData>) {
        if (today.isNotEmpty()) {
            var humidity = 0f
            var maxUVIndex = 0f
            var precipitationSum = 0.0
            today.forEach {
                humidity += it.humidity.toFloat()
                precipitationSum += it.precipitation
                maxUVIndex = max(it.uvIndex, maxUVIndex)
            }
            _averageHumidity.value = humidity / today.size
            _maxUvIndex.value = maxUVIndex
            _precipitationSum.value =
                precipitationSum.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
        }
    }


    fun getWeatherDataRoomDB() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val weatherData = withContext(Dispatchers.IO) {
                    weatherRepository.getCachedOrThrow()
                }
                _weatherData.value = weatherData
                getCurrentWeather()
                getDailyWeather()
                _errorMessage.value = null
            } catch (e: Exception) {
                Log.e("VM", "No cached weather data available", e)
                // Reset state
                _weatherData.value = WeatherData()
                _currentWeather.value = Pair(null, null)
                _dailyWeather.value = null
                _errorMessage.value = "No cached data available. Please connect to the internet."
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun getWeatherData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                locationTracker.getCurrentLocation()?.let { location ->
//                    val locationName = withContext(Dispatchers.IO){
                    val locationName = locationTracker.getLocationNameFromCoordinates(
                        location.latitude, location.longitude
                    )
//                    }
                    _locationName.value = locationName
                    val response = weatherRepository.getWeatherData(
                        location.latitude, location.longitude, locationName
                    ) // Make API call
                    _weatherData.value = response
                } ?: run {
                    getWeatherDataRoomDB()
                }

            } catch (e: IOException) {
                _errorMessage.value = "Error fetching weather data: ${e.localizedMessage}"
                getWeatherDataRoomDB()

            } catch (e: Exception) {
                _errorMessage.value = "Error fetching weather data: ${e.localizedMessage}"
            } finally {
                getCurrentWeather()
                getDailyWeather()
                _isLoading.value = false
            }
        }
    }

    fun getDailyWeather() {
        _weatherData.let {
            _dailyWeather.value =
                weatherRepository.getCurrentDailyWeather(_weatherData.value.dailyData)
        }
    }

    fun getCurrentWeather() {
        _weatherData.let {
            _currentWeather.value = weatherRepository.getCurrentWeather(
                _weatherData.value.hourlyData, _weatherData.value.airQuality
            )
        }
    }


    fun setClickedDailyWeather(currentDay: WeatherDailyData) {
        _selectedDailyWeather.value = currentDay
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setClickedHourlyWeather(currentDate: String) {
        _selectedHourlyWeather.value = _weatherData.value.hourlyData.filter {
            val formattedDate = it.time.substringBefore("T")
            formattedDate == currentDate
        }
        calculateFutureMetrics(_selectedHourlyWeather.value)
    }
}



