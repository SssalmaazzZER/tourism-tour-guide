package com.example.tourismguide.data.repository

import com.example.tourismguide.data.local.dao.GuideDao
import com.example.tourismguide.data.local.entity.GuideEntity
import com.example.tourismguide.data.remote.ApiService
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.repository.GuideRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GuideRepositoryImpl @Inject constructor(
    private val guideDao: GuideDao,
    private val apiService: ApiService,
    private val firestore: FirebaseFirestore
) : GuideRepository {
    override fun getGuides(language: String?, minRating: Double?, maxPrice: Double?): Flow<NetworkResult<List<GuideEntity>>> = flow {
        emit(NetworkResult.Loading)
        refreshGuides(language, minRating, maxPrice)
        val source = if (!language.isNullOrBlank() || minRating != null || maxPrice != null) {
            guideDao.observeFiltered(language.orEmpty(), minRating ?: 0.0, maxPrice ?: Double.MAX_VALUE)
        } else {
            guideDao.observeAll()
        }
        emitAll(source.map { NetworkResult.Success(it) })
    }.catch { emit(NetworkResult.Error(it.message ?: "Unable to load guides")) }

    override fun getGuideById(id: String): Flow<GuideEntity?> = guideDao.observeById(id)

    override fun getOnlineGuides(): Flow<List<GuideEntity>> = callbackFlow {
        val listener = firestore.collection("guide_locations").addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(emptyList())
                return@addSnapshotListener
            }
            val cutoff = System.currentTimeMillis() - ONLINE_WINDOW_MS
            val ids = snapshot?.documents.orEmpty().filter { doc ->
                val lastSeen = doc.getTimestamp("lastSeen")?.toDate()?.time ?: 0L
                doc.getBoolean("isOnline") == true && lastSeen >= cutoff
            }.map { it.id }.toSet()
            
            launch {
                runCatching {
                    val guides = guideDao.getAll().filter { it.id in ids }
                    ids.forEach { guideDao.updateOnlineStatus(it, true) }
                    trySend(guides)
                }
            }
        }
        awaitClose { listener.remove() }
    }

    override suspend fun updateGuideOnlineStatus(guideId: String, isOnline: Boolean): NetworkResult<Unit> = try {
        firestore.collection("guide_locations").document(guideId)
            .set(mapOf("isOnline" to isOnline, "lastSeen" to Timestamp.now()), com.google.firebase.firestore.SetOptions.merge())
            .await()
        guideDao.updateOnlineStatus(guideId, isOnline)
        NetworkResult.Success(Unit)
    } catch (exception: Exception) {
        NetworkResult.Error(exception.message ?: "Unable to update guide status")
    }

    private suspend fun refreshGuides(language: String?, minRating: Double?, maxPrice: Double?) {
        runCatching {
            apiService.getGuides(language, minRating, maxPrice).map {
                GuideEntity(it.id, it.name, it.avatarUrl, it.languages, it.rating, it.pricePerHour, it.phone, it.specialities, it.isVerified, it.isOnline)
            }
        }.onSuccess {
            guideDao.upsertAll(it.ifEmpty { fallbackGuides() })
        }.onFailure {
            if (guideDao.getAll().isEmpty()) guideDao.upsertAll(fallbackGuides())
        }
    }

    private fun fallbackGuides() = listOf(
        GuideEntity("guide-amina", "Amina El Idrissi", "", "AR,FR,EN", 4.9, 180.0, "212600000001", "Culture,Medina,Food", true, true),
        GuideEntity("guide-youssef", "Youssef Benali", "", "AR,FR", 4.7, 140.0, "212600000002", "Adventure,Atlas,Cities", true, false),
        GuideEntity("guide-sara", "Sara Mansouri", "", "EN,FR", 4.8, 220.0, "212600000003", "Food,Photography,Beaches", true, true)
    )

    private companion object {
        const val ONLINE_WINDOW_MS = 5 * 60 * 1000L
    }
}
