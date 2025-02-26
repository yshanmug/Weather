package com.app.weather.location
import android.Manifest
import android.app.Application
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import javax.inject.Inject
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.traceEventStart
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class DefaultLocationTracker @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application
): LocationTracker {
    override suspend fun getCurrentLocation(): Location? {
       val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isFineLocationHasPermission = ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        Log.d("FineLocationAccess", isFineLocationHasPermission.toString())
        val isCoarseLocationHasPermission = ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        Log.d("CoarseLocation", isCoarseLocationHasPermission.toString())

        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        val isnetworkenables = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        val islocationenables = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        Log.d("isnetworkenables", isnetworkenables.toString())
        Log.d("islocationenables", islocationenables.toString())

        return suspendCancellableCoroutine {cont ->
            locationClient.lastLocation.apply{
                if(isComplete){
                    if(isSuccessful){
                        cont.resume(result)
                    }
                    else{
                        cont.resume(null)
                    }
                    return@suspendCancellableCoroutine

                }

                addOnSuccessListener{cont.resume(it)}
                addOnFailureListener{cont.resume(null)}
                addOnCanceledListener{cont.cancel()}
            }
        }
    }
}


