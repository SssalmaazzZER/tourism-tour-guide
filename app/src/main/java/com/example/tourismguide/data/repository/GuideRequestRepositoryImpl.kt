package com.example.tourismguide.data.repository

import com.example.tourismguide.data.local.dao.GuideRequestDao
import com.example.tourismguide.data.local.entity.GuideRequestEntity
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.repository.GuideRequestRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GuideRequestRepositoryImpl @Inject constructor(
    private val guideRequestDao: GuideRequestDao,
    private val firestore: FirebaseFirestore
) : GuideRequestRepository {
    override fun sendRequest(request: GuideRequestEntity): Flow<NetworkResult<GuideRequestEntity>> = flow {
        emit(NetworkResult.Loading)
        try {
            guideRequestDao.upsert(request)
            firestore.collection(COLLECTION).document(request.id).set(request.toMap()).await()
            emit(NetworkResult.Success(request))
        } catch (exception: Exception) {
            emit(NetworkResult.Error(exception.message ?: "Unable to send guide request"))
        }
    }

    override fun listenToRequest(requestId: String): Flow<GuideRequestEntity?> = callbackFlow {
        val listener = firestore.collection(COLLECTION).document(requestId).addSnapshotListener { snapshot, _ ->
            val entity = snapshot?.toGuideRequest()
            if (entity != null) {
                launch {
                    runCatching { guideRequestDao.upsert(entity) }
                }
            }
            trySend(entity)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun acceptRequest(requestId: String, guideId: String): NetworkResult<Unit> = updateStatus(requestId, "CONFIRMED", mapOf("guideId" to guideId))

    override suspend fun declineRequest(requestId: String): NetworkResult<Unit> = updateStatus(requestId, "DECLINED")

    override fun getUserRequests(userId: String): Flow<List<GuideRequestEntity>> = guideRequestDao.observeByUser(userId)

    override fun getGuideIncomingRequests(guideId: String): Flow<List<GuideRequestEntity>> = callbackFlow {
        val listener = firestore.collection(COLLECTION)
            .whereEqualTo("guideId", guideId)
            .whereEqualTo("status", "PENDING")
            .addSnapshotListener { snapshot, _ ->
                val requests = snapshot?.documents.orEmpty().mapNotNull { it.toGuideRequest() }
                launch {
                    runCatching {
                        requests.forEach { guideRequestDao.upsert(it) }
                    }
                }
                trySend(requests)
            }
        awaitClose { listener.remove() }
    }

    private suspend fun updateStatus(requestId: String, status: String, extra: Map<String, Any> = emptyMap()): NetworkResult<Unit> = try {
        firestore.collection(COLLECTION).document(requestId).set(extra + mapOf("status" to status), SetOptions.merge()).await()
        guideRequestDao.updateStatus(requestId, status)
        NetworkResult.Success(Unit)
    } catch (exception: Exception) {
        NetworkResult.Error(exception.message ?: "Unable to update request")
    }

    private fun GuideRequestEntity.toMap() = mapOf(
        "id" to id,
        "userId" to userId,
        "guideId" to guideId,
        "status" to status,
        "requestedDate" to requestedDate,
        "durationHours" to durationHours,
        "peopleCount" to peopleCount,
        "startLat" to startLat,
        "startLng" to startLng,
        "specialRequests" to specialRequests,
        "estimatedPrice" to estimatedPrice,
        "createdAt" to createdAt
    )

    private fun com.google.firebase.firestore.DocumentSnapshot.toGuideRequest(): GuideRequestEntity? = try {
        GuideRequestEntity(
            id = getString("id") ?: id,
            userId = getString("userId").orEmpty(),
            guideId = getString("guideId").orEmpty(),
            status = getString("status") ?: "PENDING",
            requestedDate = getLong("requestedDate") ?: 0L,
            durationHours = (getLong("durationHours") ?: 1L).toInt(),
            peopleCount = (getLong("peopleCount") ?: 1L).toInt(),
            startLat = getDouble("startLat") ?: 0.0,
            startLng = getDouble("startLng") ?: 0.0,
            specialRequests = getString("specialRequests").orEmpty(),
            estimatedPrice = getDouble("estimatedPrice") ?: 0.0,
            createdAt = getLong("createdAt") ?: System.currentTimeMillis()
        )
    } catch (_: Exception) {
        null
    }

    private companion object {
        const val COLLECTION = "guide_requests"
    }
}
