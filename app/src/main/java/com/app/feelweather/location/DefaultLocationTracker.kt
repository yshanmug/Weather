package com.app.feelweather.location

import android.Manifest
import android.app.Application
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import javax.inject.Inject
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class DefaultLocationTracker @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application,
) : LocationTracker {
    override suspend fun getCurrentLocation(): Location? {
        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val isFineLocationHasPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val isCoarseLocationHasPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        return suspendCancellableCoroutine { cont ->
            locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).apply {
                if (isComplete) {
                    if (isSuccessful) {
                        cont.resume(result)
                    } else {
                        cont.resume(null)
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener { cont.resume(it) }
                addOnFailureListener { cont.resume(null) }
                addOnCanceledListener { cont.cancel() }
            }

        }

    }

    override suspend fun getLocationNameFromCoordinates(
        latitude: Double,
        longitude: Double,
    ): String = withContext(Dispatchers.IO) {
        val geocoder = Geocoder(application, java.util.Locale.getDefault())
        var locationName = " "
        Log.d("CurrentThread", Thread.currentThread().name)
        try {
            Log.d("CurrentThreadTry", Thread.currentThread().name)
            val address = geocoder.getFromLocation(latitude, longitude, 1)
            Log.d("CurrentThreadTry1", Thread.currentThread().name)
            Log.d("addressObjects", address.toString())
            address?.firstOrNull()?.let { address ->
                address.locality
//                    ?:address.subAdminArea?: address.adminArea   //For further info
                Log.d("Address", address.locality.toString())
                locationName = address.locality
            }

        } catch (e: Exception) {
            null
        }
        return@withContext locationName
    }
}


