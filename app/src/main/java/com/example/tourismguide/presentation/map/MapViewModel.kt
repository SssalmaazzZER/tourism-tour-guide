package com.example.tourismguide.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.repository.GuideRepository
import com.example.tourismguide.domain.usecase.GetNearbyPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MapViewModel @Inject constructor(
    getNearbyPlacesUseCase: GetNearbyPlacesUseCase,
    guideRepository: GuideRepository
) : ViewModel() {
    val placesState: StateFlow<PlacesState> = getNearbyPlacesUseCase(DEFAULT_LAT, DEFAULT_LNG, DEFAULT_RADIUS_KM, null)
        .map {
            when (it) {
                NetworkResult.Loading -> LayerState.Loading
                is NetworkResult.Error -> LayerState.Error(it.message)
                is NetworkResult.Success -> LayerState.Success(it.data)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LayerState.Loading)

    val guidesState: StateFlow<GuidesState> = guideRepository.getGuides()
        .map { result ->
            when (result) {
                NetworkResult.Loading -> LayerState.Loading
                is NetworkResult.Error -> LayerState.Error(result.message)
                is NetworkResult.Success -> LayerState.Success(result.data)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LayerState.Loading)

    private val _userLocation = MutableStateFlow<UserLocation?>(null)
    val userLocation: StateFlow<UserLocation?> = _userLocation.asStateFlow()

    fun updateUserLocation(location: UserLocation) {
        _userLocation.value = location
    }

    private companion object {
        const val DEFAULT_LAT = 33.5731
        const val DEFAULT_LNG = -7.5898
        const val DEFAULT_RADIUS_KM = 100.0
    }
}
