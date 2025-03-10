package com.app.weather.location

import android.location.Location


interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
//    suspend fun isLocationEnabled(): Boolean
}