package com.example.tourismguide.domain.usecase

import com.example.tourismguide.domain.repository.GuideRepository
import javax.inject.Inject

class SearchGuidesUseCase @Inject constructor(
    private val guideRepository: GuideRepository
) {
    operator fun invoke(language: String?, minRating: Double?, maxPrice: Double?) =
        guideRepository.getGuides(language, minRating, maxPrice)
}
