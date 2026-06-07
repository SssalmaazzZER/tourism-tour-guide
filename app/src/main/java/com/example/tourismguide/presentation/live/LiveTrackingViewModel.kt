package com.example.tourismguide.presentation.live

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.data.local.entity.GuideEntity
import com.example.tourismguide.data.local.entity.GuideRequestEntity
import com.example.tourismguide.domain.model.GuideLocation
import com.example.tourismguide.domain.repository.GuideRepository
import com.example.tourismguide.domain.repository.GuideRequestRepository
import com.example.tourismguide.domain.usecase.TrackGuideLocationUseCase
import com.example.tourismguide.util.PermissionHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LiveTrackingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val trackGuideLocationUseCase: TrackGuideLocationUseCase,
    private val guideRequestRepository: GuideRequestRepository,
    guideRepository: GuideRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val requestId: String = savedStateHandle["requestId"] ?: ""
    val guideId: String = savedStateHandle["guideId"] ?: ""

    val guide: StateFlow<GuideEntity?> = guideRepository.getGuideById(guideId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val guideLocation: StateFlow<GuideLocation?> = trackGuideLocationUseCase(guideId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val requestState: StateFlow<GuideRequestEntity?> = guideRequestRepository.listenToRequest(requestId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation.asStateFlow()

    val etaText: StateFlow<String> = combine(userLocation, guideLocation) { user, guide ->
        if (user == null || guide == null) {
            ""
        } else {
            val meters = SphericalUtil.computeDistanceBetween(user, LatLng(guide.latitude, guide.longitude))
            val minutes = ((meters / 1000.0) / 30.0 * 60.0).coerceAtLeast(1.0)
            "~${minutes.toInt()} min away"
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val requestStatus: StateFlow<String> = requestState.map { it?.status.orEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    private val _canCancel = MutableStateFlow(false)
    val canCancel: StateFlow<Boolean> = _canCancel.asStateFlow()
    private var cancelJob: Job? = null
    private var locationCallback: LocationCallback? = null

    init {
        viewModelScope.launch {
            requestState.collect { request ->
                val canCancelNow = request?.let { System.currentTimeMillis() - it.createdAt <= 120_000 } ?: false
                _canCancel.value = canCancelNow
                cancelJob?.cancel()
                if (canCancelNow && request != null) {
                    val remaining = (120_000L - (System.currentTimeMillis() - request.createdAt)).coerceAtLeast(0L)
                    cancelJob = launch {
                        delay(remaining)
                        _canCancel.value = false
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startUserLocationTracking() {
        if (!PermissionHelper.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) return
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5_000L)
            .setMinUpdateIntervalMillis(3_000L)
            .build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { _userLocation.value = LatLng(it.latitude, it.longitude) }
            }
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let { _userLocation.value = LatLng(it.latitude, it.longitude) }
        }
        fusedLocationProviderClient.requestLocationUpdates(request, locationCallback!!, context.mainLooper)
    }

    fun stopUserLocationTracking() {
        locationCallback?.let { fusedLocationProviderClient.removeLocationUpdates(it) }
        locationCallback = null
    }

    override fun onCleared() {
        stopUserLocationTracking()
        cancelJob?.cancel()
        super.onCleared()
    }
}
