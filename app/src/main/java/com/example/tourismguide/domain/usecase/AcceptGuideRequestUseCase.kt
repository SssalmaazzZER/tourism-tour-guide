package com.example.tourismguide.domain.usecase

import com.example.tourismguide.domain.repository.GuideRequestRepository
import javax.inject.Inject

class AcceptGuideRequestUseCase @Inject constructor(
    private val guideRequestRepository: GuideRequestRepository
) {
    suspend operator fun invoke(requestId: String, guideId: String) =
        guideRequestRepository.acceptRequest(requestId, guideId)
}
