package com.example.tourismguide.data.repository

import com.example.tourismguide.data.local.dao.ItineraryDao
import com.example.tourismguide.data.local.entity.ItineraryEntity
import com.example.tourismguide.domain.repository.ItineraryRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray

class ItineraryRepositoryImpl @Inject constructor(
    private val itineraryDao: ItineraryDao
) : ItineraryRepository {
    override fun getUserItineraries(userId: String): Flow<List<ItineraryEntity>> =
        itineraryDao.observeByUser(userId)

    override fun getItineraryById(id: String): Flow<ItineraryEntity?> = itineraryDao.observeById(id)

    override suspend fun createItinerary(name: String, userId: String): Long {
        val now = System.currentTimeMillis()
        val id = now
        itineraryDao.upsert(
            ItineraryEntity(
                id = id.toString(),
                userId = userId,
                name = name,
                placeIds = JSONArray().toString(),
                startDate = now,
                createdAt = now
            )
        )
        return id
    }

    override suspend fun restoreItinerary(itinerary: ItineraryEntity) {
        itineraryDao.upsert(itinerary)
    }

    override suspend fun addPlaceToItinerary(itineraryId: String, placeId: String) {
        val itinerary = itineraryDao.getById(itineraryId) ?: return
        val ids = itinerary.placeIds.toMutableSet()
        ids.add(placeId)
        itineraryDao.updatePlaceIds(itineraryId, ids.toJson())
    }

    override suspend fun deletePlaceFromItinerary(itineraryId: String, placeId: String) {
        val itinerary = itineraryDao.getById(itineraryId) ?: return
        val ids = itinerary.placeIds.toMutableSet()
        ids.remove(placeId)
        itineraryDao.updatePlaceIds(itineraryId, ids.toJson())
    }

    override suspend fun updatePlaceOrder(itineraryId: String, placeIds: List<String>) {
        itineraryDao.updatePlaceIds(itineraryId, placeIds.toJson())
    }

    override suspend fun deleteItinerary(id: String) {
        itineraryDao.deleteById(id)
    }

    private fun String.toMutableSet(): MutableSet<String> {
        val array = runCatching { JSONArray(this) }.getOrDefault(JSONArray())
        return buildSet {
            for (index in 0 until array.length()) add(array.optString(index))
        }.filter { it.isNotBlank() }.toMutableSet()
    }

    private fun Set<String>.toJson(): String {
        val array = JSONArray()
        forEach { array.put(it) }
        return array.toString()
    }

    private fun List<String>.toJson(): String {
        val array = JSONArray()
        forEach { array.put(it) }
        return array.toString()
    }
}
