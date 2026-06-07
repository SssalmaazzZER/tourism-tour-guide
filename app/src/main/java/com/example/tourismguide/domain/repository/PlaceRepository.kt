package com.example.tourismguide.domain.repository

import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.model.Place
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    fun getNearbyPlaces(lat: Double, lng: Double, radius: Double, category: String?): Flow<NetworkResult<List<Place>>>
    fun getPlaceById(id: String): Flow<NetworkResult<Place>>
    suspend fun savePlace(id: String): NetworkResult<Unit>
    suspend fun unsavePlace(id: String): NetworkResult<Unit>
    suspend fun getPopularPlaces(): List<Place>
    suspend fun getAllPlaces(): List<Place>
}
