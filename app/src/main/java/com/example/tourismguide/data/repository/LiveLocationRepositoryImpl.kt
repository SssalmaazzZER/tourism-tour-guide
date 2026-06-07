package com.example.tourismguide.data.repository

import com.example.tourismguide.domain.model.GuideLocation
import com.example.tourismguide.domain.repository.LiveLocationRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/*
Firestore rules: guide_locations write only if auth.uid == guideId, read if authenticated.
*/
class LiveLocationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : LiveLocationRepository {
    override fun listenToGuideLocation(guideId: String): Flow<GuideLocation?> = callbackFlow {
        val listener = firestore.collection("guide_locations").document(guideId).addSnapshotListener { snapshot, _ ->
            trySend(
                snapshot?.let {
                    GuideLocation(
                        guideId = it.id,
                        latitude = it.getDouble("latitude") ?: 0.0,
                        longitude = it.getDouble("longitude") ?: 0.0,
                        isOnline = it.getBoolean("isOnline") == true,
                        lastSeen = it.getTimestamp("lastSeen") ?: Timestamp.now()
                    )
                }
            )
        }
        awaitClose { listener.remove() }
    }

    override suspend fun updateMyLocation(guideId: String, lat: Double, lng: Double) {
        firestore.collection("guide_locations").document(guideId)
            .set(mapOf("latitude" to lat, "longitude" to lng, "isOnline" to true, "lastSeen" to Timestamp.now()), SetOptions.merge())
            .await()
    }
}
