package com.example.tourismguide.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tourismguide.R
import com.example.tourismguide.presentation.place.PlaceDetailActivity
import com.example.tourismguide.presentation.tourism.TourismDetailActivity

class NotificationHelper(private val context: Context) {
    fun createChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(NotificationChannel(CHANNEL_LANDMARKS, context.getString(R.string.channel_landmarks), NotificationManager.IMPORTANCE_DEFAULT))
        manager.createNotificationChannel(NotificationChannel(CHANNEL_BOOKINGS, context.getString(R.string.channel_bookings), NotificationManager.IMPORTANCE_DEFAULT))
        manager.createNotificationChannel(NotificationChannel(CHANNEL_FESTIVALS, context.getString(R.string.channel_festivals), NotificationManager.IMPORTANCE_DEFAULT))
        manager.createNotificationChannel(NotificationChannel(CHANNEL_UNESCO, context.getString(R.string.channel_unesco), NotificationManager.IMPORTANCE_HIGH))
    }

    fun showFestivalReminder(festivalId: String, festivalName: String) {
        createChannels()
        val intent = Intent(context, TourismDetailActivity::class.java).putExtra(TourismDetailActivity.EXTRA_CONTENT_ID, festivalId)
        val pendingIntent = PendingIntent.getActivity(context, festivalId.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        show(CHANNEL_FESTIVALS, festivalId.hashCode(), context.getString(R.string.festival_reminder_title, festivalName), context.getString(R.string.festival_reminder_body), pendingIntent)
    }

    fun showUnescoNotification(siteId: String, siteName: String) {
        createChannels()
        val intent = Intent(context, TourismDetailActivity::class.java).putExtra(TourismDetailActivity.EXTRA_CONTENT_ID, siteId)
        val pendingIntent = PendingIntent.getActivity(context, siteId.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        show(CHANNEL_UNESCO, siteId.hashCode(), context.getString(R.string.unesco_nearby_title, siteName), context.getString(R.string.unesco_nearby_body), pendingIntent)
    }

    fun showLandmarkNotification(placeName: String) {
        showLandmarkNotification(placeName, placeName)
    }

    fun showLandmarkNotification(placeId: String, placeName: String) {
        createChannels()
        val intent = Intent(context, PlaceDetailActivity::class.java).putExtra("placeId", placeId)
        val pendingIntent = PendingIntent.getActivity(context, placeName.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        show(CHANNEL_LANDMARKS, placeName.hashCode(), context.getString(R.string.near_landmark_title, placeName), context.getString(R.string.near_landmark_body), pendingIntent)
    }

    fun showBookingNotification(title: String, body: String) {
        createChannels()
        show(CHANNEL_BOOKINGS, title.hashCode(), title, body, null)
    }

    private fun show(channel: String, id: Int, title: String, body: String, pendingIntent: PendingIntent?) {
        val notification = NotificationCompat.Builder(context, channel)
            .setSmallIcon(R.drawable.ic_place_marker)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        if (PermissionHelper.checkPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            NotificationManagerCompat.from(context).notify(id, notification)
        }
    }

    companion object {
        const val CHANNEL_LANDMARKS = "nearby_landmarks"
        const val CHANNEL_BOOKINGS = "bookings_requests"
        const val CHANNEL_FESTIVALS = "festival_reminders"
        const val CHANNEL_UNESCO = "unesco_geofence"
    }
}
