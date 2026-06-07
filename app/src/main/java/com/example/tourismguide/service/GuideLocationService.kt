package com.example.tourismguide.service

import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.tourismguide.R
import com.example.tourismguide.data.preferences.DataStoreManager
import com.example.tourismguide.domain.repository.LiveLocationRepository
import com.example.tourismguide.util.NotificationHelper
import com.example.tourismguide.util.PermissionHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class GuideLocationService : Service() {
    @Inject lateinit var dataStoreManager: DataStoreManager
    @Inject lateinit var liveLocationRepository: LiveLocationRepository
    private lateinit var fused: FusedLocationProviderClient
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var currentIntent: Intent? = null

    override fun onCreate() {
        super.onCreate()
        fused = LocationServices.getFusedLocationProviderClient(this)
        NotificationHelper(this).createChannels()
        startForeground(1001, notification())
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        currentIntent = intent
        startUpdates()
        return START_STICKY
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private fun startUpdates() {
        if (!PermissionHelper.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) return
        val guideId = intentGuideId()
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setMinUpdateIntervalMillis(3000L)
            .build()
        fused.requestLocationUpdates(request, object : com.google.android.gms.location.LocationCallback() {
            override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
                val loc = result.lastLocation ?: return
                scope.launch { liveLocationRepository.updateMyLocation(guideId, loc.latitude, loc.longitude) }
            }
        }, mainLooper)
    }

    private fun intentGuideId(): String {
        val extra = currentIntent?.getStringExtra(EXTRA_GUIDE_ID).orEmpty()
        if (extra.isNotBlank()) return extra
        return runCatching { kotlinx.coroutines.runBlocking { dataStoreManager.userId.first().orEmpty() } }.getOrDefault("")
    }

    private fun notification(): Notification =
        NotificationCompat.Builder(this, NotificationHelper.CHANNEL_BOOKINGS)
            .setSmallIcon(R.drawable.ic_place_marker)
            .setContentTitle(getString(R.string.location_service_title))
            .setContentText(getString(R.string.tour_sharing_location))
            .setOngoing(true)
            .build()

    companion object {
        const val EXTRA_GUIDE_ID = "guideId"
    }
}
