package com.example.tourismguide.presentation.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.data.local.entity.BookingEntity
import com.example.tourismguide.data.local.entity.GuideRequestEntity
import com.example.tourismguide.data.preferences.DataStoreManager
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.repository.BookingRepository
import com.example.tourismguide.domain.repository.GuideRequestRepository
import com.example.tourismguide.domain.usecase.SendGuideRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class BookingEvent {
    data class NavigateToLiveTracking(val requestId: String, val guideId: String) : BookingEvent()
    data class Error(val message: String) : BookingEvent()
}

data class BookingUiState(val loading: Boolean = false, val message: String? = null)

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val guideRequestRepository: GuideRequestRepository,
    private val sendGuideRequestUseCase: SendGuideRequestUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _state = MutableStateFlow(BookingUiState())
    val state: StateFlow<BookingUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<BookingEvent>()
    val events: SharedFlow<BookingEvent> = _events.asSharedFlow()

    fun confirm(guideId: String, placeId: String, date: Long, durationHours: Int, totalAmount: Double) {
        viewModelScope.launch {
            _state.value = BookingUiState(loading = true)
            val userId = dataStoreManager.userId.first().orEmpty()
            val booking = BookingEntity(UUID.randomUUID().toString(), userId, guideId, placeId, date, durationHours, "PENDING", totalAmount)
            when (bookingRepository.createBooking(booking)) {
                is NetworkResult.Error -> {
                    _state.value = BookingUiState(loading = false, message = "Unable to create booking")
                    _events.emit(BookingEvent.Error("Unable to create booking"))
                    return@launch
                }
                NetworkResult.Loading -> Unit
                is NetworkResult.Success -> Unit
            }

            sendGuideRequestUseCase(
                userId = userId,
                guideId = guideId,
                placeId = placeId,
                requestedDate = date,
                durationHours = durationHours,
                peopleCount = 1,
                startLat = 0.0,
                startLng = 0.0,
                specialRequests = "Booking created",
                estimatedPrice = totalAmount
            ).collect { result ->
                when (result) {
                    NetworkResult.Loading -> _state.value = BookingUiState(loading = true)
                    is NetworkResult.Success -> {
                        _state.value = BookingUiState(loading = false, message = "Booking confirmed")
                        _events.emit(BookingEvent.NavigateToLiveTracking(result.data.id, guideId))
                    }
                    is NetworkResult.Error -> {
                        _state.value = BookingUiState(loading = false, message = result.message)
                        _events.emit(BookingEvent.Error(result.message))
                    }
                }
            }
        }
    }
}
