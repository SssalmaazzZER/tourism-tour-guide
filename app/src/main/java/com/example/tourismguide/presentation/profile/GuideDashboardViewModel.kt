package com.example.tourismguide.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.data.local.entity.GuideRequestEntity
import com.example.tourismguide.data.preferences.DataStoreManager
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.repository.GuideRequestRepository
import com.example.tourismguide.domain.usecase.AcceptGuideRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class GuideDashboardEvent {
    data class Accepted(val requestId: String, val guideId: String) : GuideDashboardEvent()
    data class Error(val message: String) : GuideDashboardEvent()
}

@HiltViewModel
class GuideDashboardViewModel @Inject constructor(
    private val guideRequestRepository: GuideRequestRepository,
    private val acceptGuideRequestUseCase: AcceptGuideRequestUseCase,
    dataStoreManager: DataStoreManager
) : ViewModel() {
    private val guideIdFlow = dataStoreManager.userId

    val incomingRequests: StateFlow<List<GuideRequestEntity>> = guideIdFlow
        .flatMapLatest { guideRequestRepository.getGuideIncomingRequests(it.orEmpty()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _events = MutableSharedFlow<GuideDashboardEvent>()
    val events: SharedFlow<GuideDashboardEvent> = _events.asSharedFlow()

    fun accept(request: GuideRequestEntity) {
        viewModelScope.launch {
            when (acceptGuideRequestUseCase(request.id, request.guideId)) {
                is NetworkResult.Success -> _events.emit(GuideDashboardEvent.Accepted(request.id, request.guideId))
                is NetworkResult.Error -> _events.emit(GuideDashboardEvent.Error("Unable to accept request"))
                NetworkResult.Loading -> Unit
            }
        }
    }

    fun decline(request: GuideRequestEntity) {
        viewModelScope.launch {
            when (guideRequestRepository.declineRequest(request.id)) {
                is NetworkResult.Success -> Unit
                is NetworkResult.Error -> _events.emit(GuideDashboardEvent.Error("Unable to decline request"))
                NetworkResult.Loading -> Unit
            }
        }
    }
}
