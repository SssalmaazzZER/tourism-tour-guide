package com.example.tourismguide.presentation.guide

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.data.local.entity.GuideEntity
import com.example.tourismguide.data.local.entity.ReviewEntity
import com.example.tourismguide.domain.repository.GuideRepository
import com.example.tourismguide.domain.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class GuideDetailUiState(val guide: GuideEntity? = null, val reviews: List<ReviewEntity> = emptyList())

@HiltViewModel
class GuideDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    guideRepository: GuideRepository,
    reviewRepository: ReviewRepository
) : ViewModel() {
    val guideId: String = savedStateHandle["guideId"] ?: ""
    val uiState: StateFlow<GuideDetailUiState> = combine(
        guideRepository.getGuideById(guideId),
        reviewRepository.getReviews(guideId, "GUIDE")
    ) { guide, reviews -> GuideDetailUiState(guide, reviews) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GuideDetailUiState())
}
