package com.example.tourismguide.domain.usecase

import com.example.tourismguide.domain.repository.LiveLocationRepository
import javax.inject.Inject

class TrackGuideLocationUseCase @Inject constructor(
    private val liveLocationRepository: LiveLocationRepository
) {
    operator fun invoke(guideId: String) = liveLocationRepository.listenToGuideLocation(guideId)
}
