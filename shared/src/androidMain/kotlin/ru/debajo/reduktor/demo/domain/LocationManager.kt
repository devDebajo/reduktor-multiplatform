package ru.debajo.reduktor.demo.domain

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine

@SuppressLint("StaticFieldLeak")
object ContextHolder {
    lateinit var context: Context
}

private class AndroidLocationManager(private val context: Context) : LocationManager {

    private val client: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(context) }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    override suspend fun getCurrentLocation(): Pair<Double, Double> {
        return suspendCancellableCoroutine { emitter ->
            val callback: LocationCallback = createListener { self, location ->
                if (emitter.isActive) {
                    emitter.resume(location, null)
                    client.removeLocationUpdates(self)
                }
            }

            client.requestLocationUpdates(
                createRequest(),
                callback,
                Looper.getMainLooper()
            )

            emitter.invokeOnCancellation { client.removeLocationUpdates(callback) }
        }
    }

    override fun isLocationAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun createListener(onNextLocation: (LocationCallback, Pair<Double, Double>) -> Unit): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(location: LocationResult) {
                val lastLocation = location.lastLocation ?: return
                onNextLocation(this, lastLocation.latitude to lastLocation.longitude)
            }
        }
    }

    private fun createRequest(): LocationRequest {
        return LocationRequest.create()
            .setInterval(5000)
            .setFastestInterval(3000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }
}

actual fun LocationManager(): LocationManager {
    return AndroidLocationManager(ContextHolder.context)
}
