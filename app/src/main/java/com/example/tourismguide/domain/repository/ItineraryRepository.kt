package com.example.tourismguide.domain.repository

import com.example.tourismguide.data.local.entity.ItineraryEntity
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
}
