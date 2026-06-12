package com.example.tourismguide.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.tourismguide.util.NotificationHelper
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent) ?: return
        if (event.hasError() || event.geofenceTransition != Geofence.GEOFENCE_TRANSITION_ENTER) return
        val requestId = event.triggeringGeofences?.firstOrNull()?.requestId.orEmpty()
        val helper = NotificationHelper(context)
        if (requestId.startsWith("unesco|")) {
            val parts = requestId.split("|")
            val siteId = parts.getOrElse(1) { "" }
            val siteName = parts.getOrElse(2) { "UNESCO site" }
            helper.showUnescoNotification(siteId, siteName)
        } else {
            val placeId = requestId.substringBefore("|", requestId)
            val placeName = requestId.substringAfter("|", requestId)
            helper.showLandmarkNotification(placeId, placeName)
        }
    }
}
