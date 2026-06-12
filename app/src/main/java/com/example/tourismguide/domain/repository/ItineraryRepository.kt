package com.example.tourismguide.domain.repository

import com.example.tourismguide.data.local.entity.ItineraryEntity
import com.example.tourismguide.data.local.entity.UserItineraryDayEntity
import kotlinx.coroutines.flow.Flow

interface ItineraryRepository {
    fun getUserItineraries(userId: String): Flow<List<ItineraryEntity>>
    fun getItineraryById(id: String): Flow<ItineraryEntity?>
    suspend fun createItinerary(name: String, userId: String): Long
    suspend fun restoreItinerary(itinerary: ItineraryEntity)
    suspend fun addPlaceToItinerary(itineraryId: String, placeId: String)
    suspend fun deletePlaceFromItinerary(itineraryId: String, placeId: String)
    suspend fun updatePlaceOrder(itineraryId: String, placeIds: List<String>)
    suspend fun deleteItinerary(id: String)
    fun observeItineraryDays(itineraryId: String): Flow<List<UserItineraryDayEntity>>
    suspend fun importCircuitDays(itineraryId: String, circuitId: String)
    suspend fun updateDayOrder(days: List<UserItineraryDayEntity>)
    suspend fun deleteDay(dayId: String)
    suspend fun addEmptyDay(itineraryId: String, title: String)
}
