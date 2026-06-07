package com.example.tourismguide.domain.repository

import com.example.tourismguide.data.local.entity.GuideEntity
import com.example.tourismguide.data.remote.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GuideRepository {
    fun getGuides(language: String? = null, minRating: Double? = null, maxPrice: Double? = null): Flow<NetworkResult<List<GuideEntity>>>
    fun getGuideById(id: String): Flow<GuideEntity?>
    fun getOnlineGuides(): Flow<List<GuideEntity>>
    suspend fun updateGuideOnlineStatus(guideId: String, isOnline: Boolean): NetworkResult<Unit>
}
