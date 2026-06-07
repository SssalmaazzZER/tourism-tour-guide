package com.example.tourismguide.domain.usecase

import com.example.tourismguide.domain.repository.PlaceRepository
import javax.inject.Inject

class GetNearbyPlacesUseCase @Inject constructor(
    private val placeRepository: PlaceRepository
) {
    operator fun invoke(lat: Double, lng: Double, radius: Double, category: String?) =
        placeRepository.getNearbyPlaces(lat, lng, radius, category)
}
