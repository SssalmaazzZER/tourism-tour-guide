package com.example.tourismguide.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.data.local.dao.UserPhotoDao
import com.example.tourismguide.data.preferences.DataStoreManager
import com.example.tourismguide.domain.repository.BookingRepository
import com.example.tourismguide.domain.repository.GuideRequestRepository
import com.example.tourismguide.domain.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ProfileViewModel @Inject constructor(
    bookingRepository: BookingRepository,
    reviewRepository: ReviewRepository,
    guideRequestRepository: GuideRequestRepository,
    userPhotoDao: UserPhotoDao,
    dataStoreManager: DataStoreManager
) : ViewModel() {
    private val currentUserId = dataStoreManager.userId
    val bookings: StateFlow<List<com.example.tourismguide.data.local.entity.BookingEntity>> =
        currentUserId.flatMapLatest { bookingRepository.getUserBookings(it.orEmpty()) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val reviews = currentUserId.flatMapLatest { reviewRepository.getMyReviews(it.orEmpty()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val requests = currentUserId.flatMapLatest { guideRequestRepository.getUserRequests(it.orEmpty()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val photos = currentUserId.flatMapLatest { userPhotoDao.observeByUser(it.orEmpty()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
