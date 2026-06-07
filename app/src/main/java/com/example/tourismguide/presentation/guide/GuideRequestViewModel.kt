package com.example.tourismguide.presentation.guide

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.data.local.entity.GuideRequestEntity
import com.example.tourismguide.data.preferences.DataStoreManager
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.repository.GuideRepository
import com.example.tourismguide.domain.repository.PlaceRepository
import com.example.tourismguide.domain.usecase.SendGuideRequestUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.tasks.await
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class GuideRequestEvent {
    data class NavigateToLiveTracking(val requestId: String, val guideId: String) : GuideRequestEvent()
    data class Error(val message: String) : GuideRequestEvent()
}

data class GuideRequestUiState(
    val isLoading: Boolean = false,
    val request: GuideRequestEntity? = null,
    val error: String? = null,
    val address: String = "",
    val startLat: Double = 0.0,
    val startLng: Double = 0.0,
    val guideName: String = "",
    val guideAvatarUrl: String = "",
    val guideRating: Double = 0.0,
    val guidePricePerHour: Double = 0.0
)

@HiltViewModel
class GuideRequestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sendGuideRequestUseCase: SendGuideRequestUseCase,
    private val guideRepository: GuideRepository,
    private val placeRepository: PlaceRepository,
    private val dataStoreManager: DataStoreManager,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val guideId: String = savedStateHandle["guideId"] ?: ""
    private val _selectedDate = MutableStateFlow(0L)
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()
    private val _selectedTime = MutableStateFlow(0L)
    val selectedTime: StateFlow<Long> = _selectedTime.asStateFlow()
    private val _durationHours = MutableStateFlow(1)
    val durationHours: StateFlow<Int> = _durationHours.asStateFlow()
    private val _peopleCount = MutableStateFlow(1)
    val peopleCount: StateFlow<Int> = _peopleCount.asStateFlow()
    private val _estimatedPrice = MutableStateFlow(0.0)
    val estimatedPrice: StateFlow<Double> = _estimatedPrice.asStateFlow()
    private val _state = MutableStateFlow(GuideRequestUiState())
    val state: StateFlow<GuideRequestUiState> = _state.asStateFlow()
    private val _events = MutableSharedFlow<GuideRequestEvent>()
    val events: SharedFlow<GuideRequestEvent> = _events.asSharedFlow()

    init {
        viewModelScope.launch { loadLocation() }
        viewModelScope.launch {
            guideRepository.getGuideById(guideId).first()?.let { guide ->
                _state.value = _state.value.copy(
                    guideName = guide.name,
                    guideAvatarUrl = guide.avatarUrl,
                    guideRating = guide.rating,
                    guidePricePerHour = guide.pricePerHour
                )
            }
            recalc()
        }
    }

    fun setDate(date: Long) { _selectedDate.value = date; recalc() }
    fun setTime(time: Long) { _selectedTime.value = time; recalc() }
    fun setDuration(hours: Int) { _durationHours.value = hours; recalc() }
    fun incrementPeople() { _peopleCount.value = (_peopleCount.value + 1).coerceAtMost(20); recalc() }
    fun decrementPeople() { _peopleCount.value = (_peopleCount.value - 1).coerceAtLeast(1); recalc() }

    fun sendRequest(specialRequests: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val userId = dataStoreManager.userId.first().orEmpty()
            val guide = guideRepository.getGuideById(guideId).first()
            if (guide == null) {
                _state.value = _state.value.copy(isLoading = false, error = "Guide not found")
                return@launch
            }
            val requestDate = selectedDate.value + selectedTime.value
            sendGuideRequestUseCase(
                userId = userId,
                guideId = guideId,
                placeId = "",
                requestedDate = requestDate,
                durationHours = durationHours.value,
                peopleCount = peopleCount.value,
                startLat = state.value.startLat,
                startLng = state.value.startLng,
                specialRequests = specialRequests,
                estimatedPrice = estimatedPrice.value
            ).collect { result ->
                when (result) {
                    NetworkResult.Loading -> _state.value = _state.value.copy(isLoading = true)
                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(isLoading = false, request = result.data)
                        _events.emit(GuideRequestEvent.NavigateToLiveTracking(result.data.id, guideId))
                    }
                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(isLoading = false, error = result.message)
                        _events.emit(GuideRequestEvent.Error(result.message))
                    }
                }
            }
        }
    }

    private suspend fun loadLocation() {
        if (!com.example.tourismguide.util.PermissionHelper.checkPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)) return
        val last = runCatching { fusedLocationProviderClient.lastLocation.await() }.getOrNull() ?: return
        val address = runCatching { Geocoder(context).getFromLocation(last.latitude, last.longitude, 1)?.firstOrNull()?.getAddressLine(0).orEmpty() }.getOrDefault("")
        _state.value = _state.value.copy(address = address, startLat = last.latitude, startLng = last.longitude)
    }

    private fun recalc() {
        _estimatedPrice.value = _durationHours.value * _peopleCount.value * _state.value.guidePricePerHour
    }
}
