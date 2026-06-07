package com.example.tourismguide.domain.repository

import com.example.tourismguide.domain.model.GuideLocation
import kotlinx.coroutines.flow.Flow

interface LiveLocationRepository {
    fun listenToGuideLocation(guideId: String): Flow<GuideLocation?>
    suspend fun updateMyLocation(guideId: String, lat: Double, lng: Double)
}
