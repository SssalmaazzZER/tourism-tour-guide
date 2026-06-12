package com.example.tourismguide.data.repository

import com.example.tourismguide.data.local.dao.CircuitDao
import com.example.tourismguide.data.local.dao.ItineraryDao
import com.example.tourismguide.data.local.dao.UserItineraryDayDao
import com.example.tourismguide.data.local.entity.ItineraryEntity
import com.example.tourismguide.data.local.entity.UserItineraryDayEntity
import com.example.tourismguide.domain.repository.ItineraryRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.json.JSONArray
import java.util.UUID

class ItineraryRepositoryImpl @Inject constructor(
    private val itineraryDao: ItineraryDao,
    private val userItineraryDayDao: UserItineraryDayDao,
    private val circuitDao: CircuitDao
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
        userItineraryDayDao.deleteByItinerary(id)
        itineraryDao.deleteById(id)
    }

    override fun observeItineraryDays(itineraryId: String): Flow<List<UserItineraryDayEntity>> =
        userItineraryDayDao.observeByItinerary(itineraryId)

    override suspend fun importCircuitDays(itineraryId: String, circuitId: String) {
        val steps = circuitDao.observeSteps(circuitId).first()
        userItineraryDayDao.deleteByItinerary(itineraryId)
        userItineraryDayDao.upsertAll(
            steps.map { step ->
                UserItineraryDayEntity(
                    id = UUID.randomUUID().toString(),
                    itineraryId = itineraryId,
                    dayOrder = step.stepOrder,
                    title = step.title,
                    description = step.description,
                    contentId = step.referenceId,
                    latitude = step.latitude,
                    longitude = step.longitude
                )
            }
        )
    }

    override suspend fun updateDayOrder(days: List<UserItineraryDayEntity>) {
        days.forEachIndexed { index, day ->
            userItineraryDayDao.upsert(day.copy(dayOrder = index + 1))
        }
    }

    override suspend fun deleteDay(dayId: String) {
        userItineraryDayDao.deleteById(dayId)
    }

    override suspend fun addEmptyDay(itineraryId: String, title: String) {
        val existing = userItineraryDayDao.observeByItinerary(itineraryId).first()
        userItineraryDayDao.upsert(
            UserItineraryDayEntity(
                id = UUID.randomUUID().toString(),
                itineraryId = itineraryId,
                dayOrder = existing.size + 1,
                title = title
            )
        )
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
