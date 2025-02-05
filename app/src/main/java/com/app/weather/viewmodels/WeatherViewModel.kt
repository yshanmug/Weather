package com.app.weather.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.weather.remote.WeatherResponseDto
import com.app.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherResponseDto?>(null)
    val weatherState: StateFlow<WeatherResponseDto?> = _weatherState

    init{

        getWeatherData(43.48, -80.56)
        Log.d("weatherResponse", _weatherState.value.toString())

    }

    fun getWeatherData(lat: Double, long: Double){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = weatherRepository.getWeatherData(lat, long) // Make API call
                _weatherState.value = response
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather data", e)
                _weatherState.value = null // Handle error gracefully
            }

        }
    }




}