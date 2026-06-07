package com.example.tourismguide.presentation.place

import com.example.tourismguide.data.local.entity.ReviewEntity
import com.example.tourismguide.data.local.entity.UserPhotoEntity
import com.example.tourismguide.domain.model.Place

data class PlaceDetailUiState(
    val isLoading: Boolean = true,
    val place: Place? = null,
    val reviews: List<ReviewEntity> = emptyList(),
    val photos: List<UserPhotoEntity> = emptyList(),
    val error: String? = null
)
