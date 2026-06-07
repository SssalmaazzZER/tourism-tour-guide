package com.example.tourismguide.service

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tourismguide.R
import com.example.tourismguide.data.preferences.DataStoreManager
import com.example.tourismguide.presentation.chat.ChatActivity
import com.example.tourismguide.presentation.booking.BookingActivity
import com.example.tourismguide.util.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {
    @Inject lateinit var dataStoreManager: DataStoreManager
    @Inject lateinit var firestore: FirebaseFirestore

    override fun onMessageReceived(message: RemoteMessage) {
        val type = message.data["type"].orEmpty()
        val title = message.data["title"] ?: message.notification?.title ?: getString(R.string.app_name)
        val body = message.data["body"] ?: message.notification?.body.orEmpty()
        val intent = when (type) {
            "CHAT_MESSAGE" -> Intent(this, ChatActivity::class.java)
            "REQUEST_CONFIRMED", "TOUR_COMPLETED" -> Intent(this, BookingActivity::class.java)
            else -> Intent()
        }
        val pendingIntent = PendingIntent.getActivity(this, type.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_BOOKINGS)
            .setSmallIcon(R.drawable.ic_place_marker)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(this).notify(type.hashCode(), notification)
    }

    override fun onNewToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreManager.setFcmToken(token)
            dataStoreManager.userId.firstOrNull()?.takeIf { it.isNotBlank() }?.let { userId ->
                firestore.collection("users").document(userId).set(mapOf("fcmToken" to token), com.google.firebase.firestore.SetOptions.merge())
            }
        }
    }
}
