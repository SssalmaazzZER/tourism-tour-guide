package com.example.tourismguide.domain.usecase

import com.example.tourismguide.data.local.entity.GuideRequestEntity
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.repository.GuideRequestRepository
import com.example.tourismguide.domain.repository.PlaceRepository
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SendGuideRequestUseCase @Inject constructor(
    private val guideRequestRepository: GuideRequestRepository,
    private val placeRepository: PlaceRepository
) {
    operator fun invoke(
        userId: String,
        guideId: String,
        placeId: String,
        requestedDate: Long,
        durationHours: Int,
        peopleCount: Int,
        startLat: Double,
        startLng: Double,
        specialRequests: String,
        estimatedPrice: Double
    ): Flow<NetworkResult<GuideRequestEntity>> {
        if (guideId.isBlank() || userId.isBlank() || requestedDate <= 0 || durationHours !in 1..8 || peopleCount !in 1..20) {
            return flow { emit(NetworkResult.Error("Please complete the request details")) }
        }
        val request = GuideRequestEntity(
            id = UUID.randomUUID().toString(),
            userId = userId,
            guideId = guideId,
            status = "PENDING",
            requestedDate = requestedDate,
            durationHours = durationHours,
            peopleCount = peopleCount,
            startLat = startLat,
            startLng = startLng,
            specialRequests = specialRequests,
            estimatedPrice = estimatedPrice,
            createdAt = System.currentTimeMillis()
        )
        return guideRequestRepository.sendRequest(request)
    }
}
