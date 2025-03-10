package com.app.weather.viewmodels

import android.util.Log
import android.util.Printer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.weather.location.LocationTracker
import com.app.weather.remote.WeatherResponseDto
import com.app.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherResponseDto?>(null)
    val weatherState: StateFlow<WeatherResponseDto?> = _weatherState

    init {
//        getWeatherData(43.48, -80.56)
//        Log.d("weatherResponse", _weatherState.value.toString())

    }

    fun getWeatherData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                locationTracker.getCurrentLocation()?.let { location ->
                    Log.d("Lati", location.latitude.toString())
                    Log.d("longi", location.longitude.toString())
                    val response = weatherRepository.getWeatherData(
                        location.latitude,
                        location.longitude
                    ) // Make API call
                    _weatherState.value = response
                } ?: run {
                    Log.e("WeatherViewModel", "Couldn't retrieve location")
                    _weatherState.value = null
                }

            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather data", e)
                _weatherState.value = null // Handle error gracefully
            }

        }
    }

}