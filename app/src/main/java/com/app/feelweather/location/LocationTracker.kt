package com.app.feelweather.location

import android.location.Location


interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
    suspend fun getLocationNameFromCoordinates(latitude: Double, longitude: Double): String
}