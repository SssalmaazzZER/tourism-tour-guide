package com.example.tourismguide.domain.repository

import com.example.tourismguide.data.local.entity.GuideRequestEntity
import com.example.tourismguide.data.remote.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GuideRequestRepository {
    fun sendRequest(request: GuideRequestEntity): Flow<NetworkResult<GuideRequestEntity>>
    fun listenToRequest(requestId: String): Flow<GuideRequestEntity?>
    suspend fun acceptRequest(requestId: String, guideId: String): NetworkResult<Unit>
    suspend fun declineRequest(requestId: String): NetworkResult<Unit>
    fun getUserRequests(userId: String): Flow<List<GuideRequestEntity>>
    fun getGuideIncomingRequests(guideId: String): Flow<List<GuideRequestEntity>>
}
