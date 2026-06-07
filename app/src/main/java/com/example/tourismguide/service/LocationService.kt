package com.example.tourismguide.service

import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.tourismguide.R
import com.example.tourismguide.util.NotificationHelper
import com.example.tourismguide.util.PermissionHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationService : Service() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location = result.lastLocation ?: return
            LocalBroadcastManager.getInstance(this@LocationService).sendBroadcast(
                Intent(ACTION_LOCATION_UPDATE)
                    .putExtra(EXTRA_LATITUDE, location.latitude)
                    .putExtra(EXTRA_LONGITUDE, location.longitude)
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        NotificationHelper(this).createChannels()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        startForeground(LOCATION_NOTIFICATION_ID, notification())
        startLocationUpdates()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(callback)
        super.onDestroy()
    }

    private fun startLocationUpdates() {
        if (!PermissionHelper.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) return
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5_000L)
            .setMinUpdateIntervalMillis(5_000L)
            .build()
        fusedLocationProviderClient.requestLocationUpdates(request, callback, mainLooper)
    }

    private fun notification(): Notification =
        NotificationCompat.Builder(this, NotificationHelper.CHANNEL_LANDMARKS)
            .setSmallIcon(R.drawable.ic_place_marker)
            .setContentTitle(getString(R.string.location_service_title))
            .setContentText(getString(R.string.location_service_body))
            .setOngoing(true)
            .build()

    companion object {
        const val ACTION_LOCATION_UPDATE = "com.example.tourismguide.LOCATION_UPDATE"
        const val EXTRA_LATITUDE = "latitude"
        const val EXTRA_LONGITUDE = "longitude"
        private const val LOCATION_NOTIFICATION_ID = 42
    }
}
